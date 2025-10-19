package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenRequest (
        @NotBlank(message = "refresh token is required")
        String refreshToken
)
{}
