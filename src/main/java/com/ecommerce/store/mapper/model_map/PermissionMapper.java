package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.PermissionResponse;
import com.ecommerce.store.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);
}
