package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.PermissionRequest;
import com.ecomerce.store.dto.response.model_response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);
    PermissionResponse update(Integer id, PermissionRequest request);
    void delete(Integer id);
    PermissionResponse findByPermissionName(String permissionName);
    List<PermissionResponse> findAll();
}
