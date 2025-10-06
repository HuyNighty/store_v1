package com.ecommerce.store.dto.request.model_request;

public record CartItemRequest(
        Integer productId,
        Integer quantity
) {
}
