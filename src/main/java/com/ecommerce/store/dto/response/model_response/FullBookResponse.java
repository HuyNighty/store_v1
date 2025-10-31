package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FullBookResponse(
        String sku,
        String slug,
        String productName,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        BigDecimal weightG,
        Boolean isActive,
        Boolean featured,

        String url,
        String fileName,
        String mimeType,
        Integer width,
        Integer height,
        Long sizeBytes,

        List<CategoryResponse> categories,

        String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        Nationality nationality
) {}
