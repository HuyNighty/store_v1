package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.RolePermissionRequest;
import com.ecommerce.store.dto.response.model_response.RolePermissionResponse;
import com.ecommerce.store.entity.Permission;
import com.ecommerce.store.entity.Role;
import com.ecommerce.store.entity.RolePermission;
import com.ecommerce.store.entity.key.RolePermissionId;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.RolePermissionMapper;
import com.ecommerce.store.repository.PermissionRepository;
import com.ecommerce.store.repository.RolePermissionRepository;
import com.ecommerce.store.repository.RoleRepository;
import com.ecommerce.store.service.model_service.RolePermissionService;
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
