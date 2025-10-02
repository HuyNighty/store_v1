package com.ecomerce.store.dto.response.model_response;

import lombok.Builder;

@Builder
public record PermissionResponse(
        Integer permissionId,
        String permissionName,
        String description
) {}
