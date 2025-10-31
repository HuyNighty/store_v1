package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record FullBookRequest(
        @NotBlank String sku,
        @NotBlank String slug,
        @NotBlank String productName,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @DecimalMin("0.0") BigDecimal salePrice,
        @Min(0) Integer stockQuantity,
        @DecimalMin("0.0") BigDecimal weightG,
        Boolean isActive,
        Boolean featured,

        @NotNull List<Integer> categoryIds,

        String url,
        String fileName,
        String mimeType,
        Integer width,
        Integer height,
        Long sizeBytes,

        String authorName,
        Integer authorId
) {}