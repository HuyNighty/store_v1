package com.ecomerce.store.dto.request.model_request;

import com.ecomerce.store.enums.entity_enums.AuthorEnums.Nationality;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthorRequest(
        @NotNull String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        @NotNull Nationality nationality,
        @NotNull Integer assetId
) {}
