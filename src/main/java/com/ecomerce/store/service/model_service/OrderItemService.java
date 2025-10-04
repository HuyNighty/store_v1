package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.OrderItemRequest;
import com.ecomerce.store.dto.response.model_response.OrderItemResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderItemService {
    OrderItemResponse addItemToOrder(Jwt jwt, Integer orderId, OrderItemRequest request);
    OrderItemResponse updateItem(Jwt jwt, Integer orderItemId, Integer newQuantity);
    void removeItem(Jwt jwt, Integer orderItemId);
    List<OrderItemResponse> getItemsByOrder(Jwt jwt, Integer orderId);
}
