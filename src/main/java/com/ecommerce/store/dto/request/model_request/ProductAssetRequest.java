package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public record ProductAssetRequest(
        Integer productId,
        Integer assetId,
        ProductAssetType type,

        @JsonSetter(nulls = Nulls.SKIP)
        @JsonProperty(defaultValue = "0")
        Integer ordinal
) {}
