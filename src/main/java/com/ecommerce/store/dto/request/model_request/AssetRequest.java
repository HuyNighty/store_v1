package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;

public record AssetRequest(
        String url,
        AssetType type,
        String fileName,
        String mimeType,
        Integer width,
        Integer height,
        Long sizeBytes
) {}
