package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.OrderItemRequest;
import com.ecommerce.store.dto.response.model_response.OrderItemResponse;
import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.OrderItem;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.OrderItemMapper;
import com.ecommerce.store.repository.OrderItemRepository;
import com.ecommerce.store.repository.OrderRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.OrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {

    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderItemMapper orderItemMapper;

    @Override
    public OrderItemResponse addItemToOrder(Jwt jwt, Integer orderId, OrderItemRequest request) {
        Order order = getOrderOfCurrentUser(jwt, orderId);

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(request.quantity())
                .unitPrice(product.getPrice())
                .build();

        orderItemRepository.save(orderItem);

        return orderItemMapper.toResponse(orderItem);
    }

    @Override
    public OrderItemResponse updateItem(Jwt jwt, Integer orderItemId, Integer newQuantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        String userId = jwt.getClaimAsString("id");
        if (!orderItem.getOrder().getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (newQuantity <= 0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        orderItem.setQuantity(newQuantity);
        orderItem.setUpdatedAt(LocalDateTime.now());

        orderItemRepository.save(orderItem);
        return orderItemMapper.toResponse(orderItem);
    }

    @Override
    public void removeItem(Jwt jwt, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        String userId = jwt.getClaimAsString("id");
        if (!orderItem.getOrder().getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        orderItemRepository.delete(orderItem);
    }

    @Override
    public List<OrderItemResponse> getItemsByOrder(Jwt jwt, Integer orderId) {
        Order order = getOrderOfCurrentUser(jwt, orderId);
        return orderItemMapper.toResponseList(orderItemRepository.findByOrderOrderId(order.getOrderId()));
    }

    private Order getOrderOfCurrentUser(Jwt jwt, Integer orderId) {
        String userId = jwt.getClaimAsString("id");
        return orderRepository.findByOrderIdAndUserUserId(orderId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

}
