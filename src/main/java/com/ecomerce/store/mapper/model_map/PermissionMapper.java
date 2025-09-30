package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.response.model_response.PermissionResponse;
import com.ecomerce.store.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);
}
