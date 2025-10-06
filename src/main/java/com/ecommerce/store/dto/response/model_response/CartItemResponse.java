package com.ecommerce.store.dto.response.model_response;

import java.math.BigDecimal;

public record CartItemResponse(
        Integer productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
