package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;
import lombok.Builder;

@Builder
public record AssetResponse(

         Integer assetId,
         String url,
         AssetType type,
         String fileName,
         String mimeType,
         Integer width,
         Integer height,
         Long sizeBytes,
         boolean deleted
) {}
