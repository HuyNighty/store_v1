package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.*;

public record UpdateCartItemQuantityRequest(

        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be > 0")
        Integer quantity
) {}
