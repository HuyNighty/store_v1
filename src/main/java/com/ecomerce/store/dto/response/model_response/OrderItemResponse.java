package com.ecomerce.store.dto.response.model_response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer orderItemId,
        Integer productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subTotal
) {}
