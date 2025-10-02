package com.ecomerce.store.dto.response.model_response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RolePermissionResponse(
        Integer roleId,
        String roleName,
        Integer permissionId,
        String permissionName,
        LocalDateTime assignedAt
) {
}

