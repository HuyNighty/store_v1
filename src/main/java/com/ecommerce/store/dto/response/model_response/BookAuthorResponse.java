package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.BookAuthorEnums.AuthorRole;

public record BookAuthorResponse(
        Integer productId,
        Integer authorId,
        String productName,
        String authorName,
        AuthorRole authorRole
) {}
