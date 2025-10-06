package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.RolePermissionRequest;
import com.ecommerce.store.dto.response.model_response.RolePermissionResponse;

import java.util.List;

public interface RolePermissionService {

    RolePermissionResponse assignPermission(RolePermissionRequest request);
    void removePermission(RolePermissionRequest request);
    List<RolePermissionResponse> getPermissionsByRole(Integer roleId);
}
