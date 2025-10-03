package com.ecomerce.store.service.impl.model_impl;

import com.ecomerce.store.dto.request.model_request.PermissionRequest;
import com.ecomerce.store.dto.response.model_response.PermissionResponse;
import com.ecomerce.store.entity.Permission;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.model_map.PermissionMapper;
import com.ecomerce.store.repository.PermissionRepository;
import com.ecomerce.store.service.model_service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByPermissionNameIgnoreCase(request.permissionName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = Permission
                .builder()
                .permissionName(request.permissionName())
                .description(request.description())
                .build();

        permissionRepository.save(permission);
        return toResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(Integer id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        if (request.permissionName() != null && !(request.permissionName().isBlank())) {
            boolean isExist = permissionRepository.existsByPermissionNameIgnoreCase(request.permissionName());
            if (isExist && !permission.getPermissionName().equalsIgnoreCase(request.permissionName())) {
                throw new AppException(ErrorCode.PERMISSION_EXISTED);
            }
            permission.setPermissionName(request.permissionName());
        }

        if (request.description() != null && !(request.description().isBlank())) {
            permission.setDescription(request.description());
        }

        permissionRepository.save(permission);

        return toResponse(permission);
    }

    @Override
    public void deletePermission(Integer id) {
        if(permissionRepository.existsById(id)){
            permissionRepository.deleteById(id);
        }
        else {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
    }

    @Override
    public PermissionResponse findByPermissionName(String permissionName) {
        Permission permission = permissionRepository.findByPermissionNameIgnoreCase(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        return toResponse(permission);
    }

    @Override
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    private PermissionResponse toResponse(Permission permission) {
        return permissionMapper.toPermissionResponse(permission);
    }
}
