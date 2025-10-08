package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.RoleRequest;
import com.ecommerce.store.dto.response.model_response.RoleResponse;
import com.ecommerce.store.entity.Role;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.RoleMapper;
import com.ecommerce.store.repository.RoleRepository;
import com.ecommerce.store.service.model_service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByRoleNameIgnoreCase(request.roleName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTED);
        }

        Role role = Role.builder()
                .roleName(request.roleName())
                .description(request.description())
                .build();

        Role savedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    public RoleResponse updateRole(Integer roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (request.roleName() != null && !request.roleName().isBlank()) {
            boolean exists = roleRepository.existsByRoleNameIgnoreCase(request.roleName());
            if (exists && !role.getRoleName().equalsIgnoreCase(request.roleName())) {
                throw new AppException(ErrorCode.ROLE_ALREADY_EXISTED);
            }
            role.setRoleName(request.roleName());
        }

        if (request.description() != null && !request.description().isBlank()) {
            role.setDescription(request.description());
        }

        role.setUpdatedAt(LocalDateTime.now());
        Role saved = roleRepository.save(role);
        return roleMapper.toRoleResponse(saved);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roleRepository.delete(role);
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse findRoleByName(String name) {
        Role role = roleRepository.findByRoleNameIgnoreCase(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return roleMapper.toRoleResponse(role);
    }
}

