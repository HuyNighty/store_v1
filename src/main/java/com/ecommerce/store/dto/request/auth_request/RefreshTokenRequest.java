package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest (
        @NotBlank
        String refreshToken
)
{}
