package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.PermissionRequest;
import com.ecomerce.store.dto.response.model_response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);
    PermissionResponse updatePermission(Integer id, PermissionRequest request);
    void deletePermission(Integer id);
    PermissionResponse findByPermissionName(String permissionName);
    List<PermissionResponse> findAll();
}
