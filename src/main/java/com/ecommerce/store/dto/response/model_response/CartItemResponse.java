package com.ecommerce.store.dto.response.model_response;

import java.math.BigDecimal;
import java.util.List;

public record CartItemResponse(
        Integer cartItemId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,

        Integer productId,
        String productName,
        String sku,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        Boolean featured,

        String url,

        String authorName,
        List<BookAuthorResponse> bookAuthors
) {
}
