package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.*;

public record RolePermissionRequest(

        @NotNull(message = "roleId is required")
        @Positive(message = "roleId must be positive")
        Integer roleId,

        @NotNull(message = "permissionId is required")
        @Positive(message = "permissionId must be positive")
        Integer permissionId
) {}
