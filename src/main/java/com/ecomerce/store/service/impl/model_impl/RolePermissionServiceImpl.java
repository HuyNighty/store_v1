package com.ecomerce.store.service.impl.model_impl;

import com.ecomerce.store.dto.request.model_request.RolePermissionRequest;
import com.ecomerce.store.dto.response.model_response.RolePermissionResponse;
import com.ecomerce.store.entity.Permission;
import com.ecomerce.store.entity.Role;
import com.ecomerce.store.entity.RolePermission;
import com.ecomerce.store.entity.key.RolePermissionId;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.model_map.RolePermissionMapper;
import com.ecomerce.store.repository.PermissionRepository;
import com.ecomerce.store.repository.RolePermissionRepository;
import com.ecomerce.store.repository.RoleRepository;
import com.ecomerce.store.service.model_service.RolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolePermissionServiceImpl implements RolePermissionService {

    RolePermissionRepository rolePermissionRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermissionResponse assignPermission(RolePermissionRequest request) {
        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Permission permission = permissionRepository.findById(request.permissionId())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        RolePermissionId id = new RolePermissionId(role.getRoleId(), permission.getPermissionId());

        boolean exists = rolePermissionRepository.existsById(id);
        if (exists) {
            throw new AppException(ErrorCode.ROLE_PERMISSION_ALREADY_EXISTED);
        }

        RolePermission rolePermission = RolePermission
                .builder()
                .rolePermissionId(id)
                .role(role)
                .permission(permission)
                .assignedAt(LocalDateTime.now())
                .build();

        rolePermission = rolePermissionRepository.save(rolePermission);

        return rolePermissionMapper.toRolePermissionResponse(rolePermission);
    }

    @Override
    public void removePermission(RolePermissionRequest request) {
        RolePermissionId id = new RolePermissionId(request.roleId(), request.permissionId());

        if (!rolePermissionRepository.existsById(id)) {
            throw new AppException(ErrorCode.ROLE_PERMISSION_NOT_FOUND);
        }

        rolePermissionRepository.deleteById(id);
    }

    @Override
    public List<RolePermissionResponse> getPermissionsByRole(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));


        List<RolePermissionResponse> permissions = rolePermissionRepository.findByRoleRoleId(roleId)
                .stream()
                .map(rolePermissionMapper::toRolePermissionResponse)
                .toList();

        if (permissions.isEmpty()) {
            return List.of(RolePermissionResponse.builder()
                    .roleId(roleId)
                    .roleName(role.getRoleName())
                    .build());
        }

        return permissions;
    }
}
