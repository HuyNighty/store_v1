package com.ecomerce.store.dto.response.model_response;

import com.ecomerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;

public record ProductAssetResponse(
        Integer productId,
        String productName,
        Integer assetId,
        String fileName,
        String url,
        ProductAssetType type,
        Integer ordinal
) {}
