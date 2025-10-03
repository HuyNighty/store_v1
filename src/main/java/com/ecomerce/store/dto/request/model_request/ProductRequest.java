package com.ecomerce.store.dto.request.model_request;

import java.math.BigDecimal;

public record ProductRequest(
        String sku,
        String slug,
        String productName,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        BigDecimal weightG,
        Boolean isActive,
        Boolean featured
) {}
