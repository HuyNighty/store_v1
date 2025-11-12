package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record AuthorRequest(
        @NotBlank(message = "Author name is required")
        String authorName,

        String bio,

        @PastOrPresent(message = "Born date cannot be in the future")
        LocalDate bornDate,

        @PastOrPresent(message = "Death date cannot be in the future")
        LocalDate deathDate,

        @NotNull(message = "Nationality is required")
        Nationality nationality,

        Integer assetId,

        String portraitUrl,

        List<Integer> galleryAssetIds,

        String wikiUrl
) {}
