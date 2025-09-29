package com.ecomerce.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PermissionRequest (

        @NotNull    @NotBlank
        String permissionName,
        String description
) {}

