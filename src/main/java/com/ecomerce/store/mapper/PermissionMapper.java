package com.ecomerce.store.mapper;

import com.ecomerce.store.dto.response.PermissionResponse;
import com.ecomerce.store.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);
}
