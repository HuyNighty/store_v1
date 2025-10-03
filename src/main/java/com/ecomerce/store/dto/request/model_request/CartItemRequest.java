package com.ecomerce.store.dto.request.model_request;

import java.math.BigDecimal;

public record CartItemRequest(
        Integer productId,
        Integer quantity
) {
}
