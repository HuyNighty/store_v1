package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.response.model_response.RoleResponse;
import com.ecomerce.store.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}
