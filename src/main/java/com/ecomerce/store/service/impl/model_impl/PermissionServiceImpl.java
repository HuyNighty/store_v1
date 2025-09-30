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
    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepository.existsByPermissionName(request.permissionName())) {
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
    public PermissionResponse update(Integer id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        if (!permission.getPermissionName().equals(request.permissionName()) &&
                permissionRepository.existsByPermissionName(request.permissionName()))
        {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        permission.setDescription(request.description());
        permissionRepository.save(permission);
        return toResponse(permission);
    }

    @Override
    public void delete(Integer id) {
        if(permissionRepository.existsById(id)){
            permissionRepository.deleteById(id);
        }
        else {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
    }

    @Override
    public PermissionResponse findById(Integer id) {
        Permission permission = permissionRepository.findById(id)
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
