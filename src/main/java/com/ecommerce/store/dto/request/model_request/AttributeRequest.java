package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AttributeEnums.DataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttributeRequest(

        @NotBlank(message = "Attribute name must not be blank")
        String attributeName,

        @NotNull(message = "Date type is required")
        DataType dataType
) {}
