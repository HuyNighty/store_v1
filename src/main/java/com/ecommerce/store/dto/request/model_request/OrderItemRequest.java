package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        Integer productId,

        @Positive(message = "Quantity must be greater than 0")
        Integer quantity
) {}
