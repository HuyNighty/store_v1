package com.ecommerce.store.dto.request.model_request;

import java.math.BigDecimal;
import java.util.List;

public record FullBookUpdateRequest(
        String sku,
        String slug,
        String productName,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        BigDecimal weightG,
        Boolean isActive,
        Boolean featured,

        List<Integer> categoryIds,

        String url,
        String fileName,
        String mimeType,
        Integer width,
        Integer height,
        Long sizeBytes,

        String authorName,
        Integer authorId
) {
}
