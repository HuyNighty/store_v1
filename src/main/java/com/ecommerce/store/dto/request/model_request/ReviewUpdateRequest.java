package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.*;

public record ReviewUpdateRequest(

        @Min(value = 0, message = "rating must be >= 0")
        @Max(value = 10, message = "rating must be <= 10")
        Float rating,

        @Size(max = 500, message = "comment too long")
        String comment
) {}
