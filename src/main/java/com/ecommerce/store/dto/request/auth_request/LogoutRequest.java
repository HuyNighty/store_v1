package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull(message = "token is required")
        String token
) {}
