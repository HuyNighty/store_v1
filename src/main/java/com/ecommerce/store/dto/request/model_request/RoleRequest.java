package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.*;

public record RoleRequest(

        @NotBlank(message = "roleName is required")
        @Size(max = 40)
        String roleName,

        @Size
        String description
) {}
