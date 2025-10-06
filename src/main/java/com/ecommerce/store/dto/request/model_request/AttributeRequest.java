package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AttributeEnums.DataType;
import jakarta.validation.constraints.NotBlank;

public record AttributeRequest(

        @NotBlank
        String attributeName,
        DataType dataType
) {}
