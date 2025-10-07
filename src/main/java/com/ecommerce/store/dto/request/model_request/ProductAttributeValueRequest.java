package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProductAttributeValueRequest(

        @NotNull(message = "productId is required")
        @Positive(message = "productId must be positive")
        Integer productId,

        @NotNull(message = "attributeId is required")
        @Positive(message = "attributeId must be positive")
        Integer attributeId,

        @Size
        String valueText,

        Double valueNumber,

        LocalDate valueDate
) {}
