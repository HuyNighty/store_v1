package com.ecomerce.store.dto.request.model_request;

import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        Integer productId,

        @Positive
        Integer quantity
) {}
