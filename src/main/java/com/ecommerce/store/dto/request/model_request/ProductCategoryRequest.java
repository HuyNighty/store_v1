package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCategoryRequest(

        @NotNull(message = "productId is required")
        @Positive(message = "productId must be positive")
        Integer productId,

        @NotNull(message = "categoryId is required")
        @Positive(message = "categoryId must be positive")
        Integer categoryId
) {}
