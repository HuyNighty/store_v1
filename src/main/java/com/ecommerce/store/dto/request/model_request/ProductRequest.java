package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "sku is required")
        @Size(max = 255)
        String sku,

        @NotBlank(message = "slug is required")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "slug must contain only lowercase letters, numbers and dashes")
        @Size(max = 255)
        String slug,

        @NotBlank(message = "productName is required")
        @Size(max = 255)
        String productName,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "price must be > 0")
        BigDecimal price,

        @DecimalMin(value = "0.0", message = "salePrice must be >= 0")
        BigDecimal salePrice,

        @Min(value = 0, message = "stockQuantity cannot be negative")
        Integer stockQuantity,

        @DecimalMin(value = "0.0", message = "weightG must be >= 0")
        BigDecimal weightG,

        Boolean isActive,
        Boolean featured
) {}
