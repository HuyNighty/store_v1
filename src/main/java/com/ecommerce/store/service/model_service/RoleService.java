package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.RoleRequest;
import com.ecommerce.store.dto.response.model_response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);
    RoleResponse updateRole(Integer roleId, RoleRequest request);
    void deleteRole(Integer roleId);
    List<RoleResponse> findAllRoles();
    RoleResponse findRoleByName(String name);
}
