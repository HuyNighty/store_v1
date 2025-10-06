package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.RolePermissionResponse;
import com.ecommerce.store.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "permission.permissionId", target = "permissionId")
    @Mapping(source = "permission.permissionName", target = "permissionName")
    RolePermissionResponse toRolePermissionResponse(RolePermission rolePermission);
}