package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.AttributeEnums.DataType;

public record AttributeResponse(
        Integer attributeId,
        String attributeName,
        DataType dataType
) {}
