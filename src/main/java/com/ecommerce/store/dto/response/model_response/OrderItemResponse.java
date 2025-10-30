package com.ecommerce.store.dto.response.model_response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer orderItemId,
        Integer productId,
        String productName,
        String productUrl,
        String categoryName,
        String authorName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subTotal
) {}
