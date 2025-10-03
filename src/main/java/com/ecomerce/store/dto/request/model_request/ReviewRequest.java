package com.ecomerce.store.dto.request.model_request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(
        @NotNull
        @Min(0)
        @Max(10)
        Float rating,

        @Size(max = 500)
        String comment
) {}
