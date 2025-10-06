package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.BookAuthorEnums.AuthorRole;

public record BookAuthorRequest(
        Integer productId,
        Integer authorId,
        AuthorRole authorRole
) {}
