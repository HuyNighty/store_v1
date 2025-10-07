package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequest (

        @NotBlank(message = "permissionName is required")
        String permissionName,

        @Size
        String description
) {}

