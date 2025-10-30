package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.OrderRequest;
import com.ecommerce.store.dto.request.model_request.UpdateStatusOrderRequest;
import com.ecommerce.store.dto.response.model_response.OrderResponse;
import com.ecommerce.store.entity.CartItem;
import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.OrderItem;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.enums.entity_enums.OrderEnums.StatusOrder;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.OrderMapper;
import com.ecommerce.store.repository.CartItemRepository;
import com.ecommerce.store.repository.OrderItemRepository;
import com.ecommerce.store.repository.OrderRepository;
import com.ecommerce.store.repository.UserRepository;
import com.ecommerce.store.service.model_service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    CartItemRepository cartItemRepository;
    OrderItemRepository orderItemRepository;
    OrderMapper orderMapper;

    @Override
    public OrderResponse createOrderFromCart(Jwt jwt, OrderRequest request) {
        String userId = jwt.getClaimAsString("id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<CartItem> cartItems = cartItemRepository.findByCartUserUserId(userId);
        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        BigDecimal totalAmount = cartItems
                .stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order
                .builder()
                .user(user)
                .orderNumber(UUID.randomUUID().toString())
                .statusOrder(StatusOrder.PENDING)
                .totalAmount(totalAmount)
                .paymentMethod(request.paymentMethod())
                .shippingAddress(request.shippingAddress())
                .note(request.note())
                .shippingCost(BigDecimal.valueOf(0))
                .build();

        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getUnitPrice())
                        .build())
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);

        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateStatus(Integer orderId, UpdateStatusOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));


        validateStatusTransition(order.getStatusOrder(), request.newStatusOrder());

        order.setStatusOrder(request.newStatusOrder());
        switch (request.newStatusOrder()) {
            case COMPLETED -> {
                order.setCompletedAt(LocalDateTime.now());
                order.getOrderItems().forEach(item -> item.setDeletedAt(LocalDateTime.now()));
            }
            case CANCELLED -> {
                order.setCanceledAt(LocalDateTime.now());
                order.getOrderItems().forEach(item -> item.setDeletedAt(LocalDateTime.now()));
            }
            case DELIVERED -> order.setDeliveredAt(LocalDateTime.now());
            default -> {}
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderRoleAdmin(Integer orderId, String userId) {
        Order order = orderRepository.findByOrderIdAndUserUserId(orderId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Jwt jwt, Integer orderId) {
        String userId = jwt.getClaimAsString("id");

        Order order = orderRepository.findByOrderIdAndUserUserId(orderId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatusOrder() == StatusOrder.CANCELLED ||
                order.getStatusOrder() == StatusOrder.COMPLETED) {
            throw new AppException(ErrorCode.CANCELED_INVALID);
        }

        if (order.getStatusOrder() == StatusOrder.PENDING ||
                order.getStatusOrder() == StatusOrder.PAID) {

            order.setStatusOrder(StatusOrder.CANCELLED);
            order.setCanceledAt(LocalDateTime.now());

            order.getOrderItems().forEach(item -> item.setDeletedAt(LocalDateTime.now()));

            orderRepository.save(order);
        } else {
            throw new AppException(ErrorCode.CANCELED_INVALID);
        }
    }

    @Override
    public void adminDeleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setDeletedAt(LocalDateTime.now());
        order.getOrderItems().forEach(item -> {
            if (item.getDeletedAt() == null) {
                item.setDeletedAt(LocalDateTime.now());
            }
        });

        orderRepository.save(order);
    }


    @Override
    public List<OrderResponse> getMyOrders(Jwt jwt) {
        String userId = jwt.getClaimAsString("id");
        List<Order> orders = orderRepository.findByUserUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Jwt jwt, Integer orderId) {
        String userId = jwt.getClaimAsString("id");
        Order oder  = orderRepository.findByOrderIdAndUserUserId(orderId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(oder);
    }

    @Override
    public void deleteOrder(Jwt jwt, Integer orderId) {
        String userId = jwt.getClaimAsString("id");

        Order order = orderRepository.findByOrderIdAndUserUserId(orderId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatusOrder() != StatusOrder.CANCELLED) {
            throw new AppException(ErrorCode.DELETE_ORDER_INVALID);
        }

        order.setDeletedAt(LocalDateTime.now());
        order.getOrderItems().forEach(item -> {
            if (item.getDeletedAt() == null) {
                item.setDeletedAt(LocalDateTime.now());
            }
        });

        orderRepository.save(order);
    }

    public void validateStatusTransition(StatusOrder current, StatusOrder next) {
        switch (current) {
            case PENDING -> {
                if (next != StatusOrder.PAID && next != StatusOrder.CANCELLED)
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            case PAID -> {
                if (next != StatusOrder.SHIPPED && next != StatusOrder.CANCELLED)
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            case SHIPPED -> {
                if (next != StatusOrder.DELIVERED)
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            case DELIVERED -> {
                if (next != StatusOrder.COMPLETED)
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            case COMPLETED, CANCELLED -> {
                throw new AppException(ErrorCode.ORDER_ALREADY_FINALIZED);
            }
            default -> throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }
}
