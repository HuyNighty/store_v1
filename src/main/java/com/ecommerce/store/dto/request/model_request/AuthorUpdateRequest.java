package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

public record AuthorUpdateRequest(
        String authorName,
        String bio,
        @PastOrPresent LocalDate bornDate,
        @PastOrPresent LocalDate deathDate,
        Nationality nationality,
        Integer assetId,
        String portraitUrl,
        List<Integer> galleryAssetIds,
        String wikiUrl
) {}
