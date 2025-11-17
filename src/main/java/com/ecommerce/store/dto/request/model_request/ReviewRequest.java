package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.*;

public record ReviewRequest(

        @NotNull(message = "rating is required")
        @Positive
        @Max(value = 5, message = "rating must be <= 5")
        Float rating,

        @Size(max = 500, message = "comment too long")
        String comment
) {}
