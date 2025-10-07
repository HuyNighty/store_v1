package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.NotNull;

public record IntrospectRequest (
        @NotNull(message = "toke is required")
        String token
){}
