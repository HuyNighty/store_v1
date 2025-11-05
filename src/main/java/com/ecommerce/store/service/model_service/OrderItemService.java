package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.OrderItemRequest;
import com.ecommerce.store.dto.response.model_response.OrderItemResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderItemService {
    OrderItemResponse addItemToOrder(Jwt jwt, Integer orderId, OrderItemRequest request);
    OrderItemResponse updateItem(Jwt jwt, Integer orderItemId, Integer newQuantity);
    void removeItem(Jwt jwt, Integer orderItemId);
    List<OrderItemResponse> getItemsByOrder(Jwt jwt, Integer orderId);
    List<OrderItemResponse> getItemsByOrderAdmin(Integer orderId);
}
