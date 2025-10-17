package com.ecommerce.store.dto.response.model_response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
        Integer productId,
        String sku,
        String slug,
        String productName,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        BigDecimal weightG,
        Boolean isActive,
        Boolean featured,
        List<ProductAssetResponse> productAssets,
        List<BookAuthorResponse> bookAuthors,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
