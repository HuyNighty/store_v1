package com.ecomerce.store.service;

import com.ecomerce.store.dto.request.PermissionRequest;
import com.ecomerce.store.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);
    PermissionResponse update(Integer id, PermissionRequest request);
    void delete(Integer id);
    PermissionResponse findById(Integer id);
    List<PermissionResponse> findAll();
}
