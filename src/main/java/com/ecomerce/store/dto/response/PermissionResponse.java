package com.ecomerce.store.dto.response;

public record PermissionResponse(
        Integer permissionId,
        String permissionName,
        String description
) {}
