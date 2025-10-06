package com.ecommerce.store.dto.response.model_response;

import java.time.LocalDate;

public record ProductAttributeValueResponse(
        Integer productId,
        Integer attributeId,
        String attributeName,
        String valueText,
        Double valueNumber,
        LocalDate valueDate
) {
}
