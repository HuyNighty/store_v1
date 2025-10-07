package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;
import jakarta.validation.constraints.*;

public record AssetRequest(
        @NotBlank(message = "URL must not be blank")
        String url,

        @NotNull(message = "Asset type is required")
        AssetType type,

        @NotBlank(message = "File name is required")
        String fileName,

        @NotBlank(message = "MIME type is required")
        String mimeType,

        @PositiveOrZero(message = "Width must be zero or positive")
        Integer width,

        @PositiveOrZero(message = "Height must be zero or positive")
        Integer height,

        @Positive(message = "File size must be greater than zero")
        Long sizeBytes
) {}
