package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AuthorResponse(
        Integer authorId,
        String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        Nationality nationality,
        Integer assetId,
        String portraitUrl,
        String wikiUrl,
        Integer totalBooks,
        Double averageRating
) {}
