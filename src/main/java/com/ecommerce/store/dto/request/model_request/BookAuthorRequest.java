package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.BookAuthorEnums.AuthorRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookAuthorRequest(

        @NotNull(message = "Product ID is required")
        @Positive
        Integer productId,

        @NotNull(message = "Author ID is required")
        @Positive
        Integer authorId,

        @NotNull
        AuthorRole authorRole
) {}
