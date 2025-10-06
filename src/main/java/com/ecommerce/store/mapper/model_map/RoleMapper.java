package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.RoleResponse;
import com.ecommerce.store.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}
