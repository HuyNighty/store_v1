package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.CartItemRequest;
import com.ecommerce.store.dto.response.model_response.CartItemResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CartItemService {
    // USER
    CartItemResponse addItemToCartForUser(Jwt jwt, CartItemRequest request);
    CartItemResponse updateItemQuantityForUser(Jwt jwt, Integer productId, Integer newQuantity);
    void removeItemFromCartForUser(Jwt jwt, Integer productId);
    List<CartItemResponse> getItemsForUser(Jwt jwt);

    // ADMIN
    List<CartItemResponse> getItemsByCartId(Integer cartId);
    void removeItemFromCart(Integer cartId, Integer productId);
}
