package com.ecommerce.store.dto.request.model_request;

import java.time.LocalDate;

public record ProductAttributeValueRequest(
        Integer productId,
        Integer attributeId,
        String valueText,
        Double valueNumber,
        LocalDate valueDate
) {
}
