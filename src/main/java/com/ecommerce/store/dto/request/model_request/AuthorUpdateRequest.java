package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;

import java.time.LocalDate;

public record AuthorUpdateRequest(
        String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        Nationality nationality,
        Integer assetId
) {}