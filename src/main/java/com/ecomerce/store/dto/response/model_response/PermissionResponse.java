package com.ecomerce.store.dto.response.model_response;

public record PermissionResponse(
        Integer permissionId,
        String permissionName,
        String description
) {}
