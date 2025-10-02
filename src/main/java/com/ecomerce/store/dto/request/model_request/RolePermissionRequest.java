package com.ecomerce.store.dto.request.model_request;

public record RolePermissionRequest(
        Integer roleId,
        Integer permissionId
) {}
