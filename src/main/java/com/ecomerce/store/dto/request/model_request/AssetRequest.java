package com.ecomerce.store.dto.request.model_request;

import com.ecomerce.store.enums.entity_enums.AssetEnums.AssetType;

public record AssetRequest(
        String url,
        AssetType type,
        String mimeType,
        Integer width,
        Integer height,
        Long sizeBytes
) {}
