package com.ecomerce.store.dto.response.model_response;

import com.ecomerce.store.enums.entity_enums.AuthorEnums.Nationality;

import java.time.LocalDate;

public record AuthorResponse(
        Integer authorId,
        String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        Nationality nationality,
        Integer assetId
) {}
