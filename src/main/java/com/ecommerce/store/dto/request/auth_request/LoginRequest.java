package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank(message = "identifier is required")
        String identifier,

        @NotBlank
        String password
){}
