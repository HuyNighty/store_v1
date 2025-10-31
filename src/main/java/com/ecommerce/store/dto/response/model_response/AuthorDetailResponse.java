package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AuthorDetailResponse(
        Integer authorId,
        String authorName,
        String bio,
        LocalDate bornDate,
        LocalDate deathDate,
        Nationality nationality,
        String portraitUrl,
        String wikiUrl,
        Integer totalBooks,
        Double averageRating,
        List<ProductResponse> books
) {}