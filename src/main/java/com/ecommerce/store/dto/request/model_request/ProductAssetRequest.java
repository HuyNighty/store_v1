package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductAssetRequest(

        @NotNull(message = "productId is required")
        @Positive(message = "productId must be positive")
        Integer productId,

        @NotNull(message = "assetId is required")
        @Positive(message = "assetId must be positive")
        Integer assetId,

        @NotNull(message = "type is required")
        ProductAssetType type,

        @JsonSetter(nulls = Nulls.SKIP)
        @JsonProperty(defaultValue = "0")
        Integer ordinal
) {}
