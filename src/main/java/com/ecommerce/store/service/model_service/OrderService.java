package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.OrderRequest;
import com.ecommerce.store.dto.request.model_request.UpdateStatusOrderRequest;
import com.ecommerce.store.dto.response.model_response.OrderResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderService {

    OrderResponse createOrderFromCart(Jwt jwt, OrderRequest request);

    List<OrderResponse> getMyOrders(Jwt jwt);

    OrderResponse getOrderById(Jwt jwt, Integer orderId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateStatus(Integer orderId, UpdateStatusOrderRequest request);

    OrderResponse getOrderRoleAdmin(Integer orderId, String userId);

    List<OrderResponse> getOrdersByUserId(String userId);

    void cancelOrder(Jwt jwt, Integer orderId);
}
