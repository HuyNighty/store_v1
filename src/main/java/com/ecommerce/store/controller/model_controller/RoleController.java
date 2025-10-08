package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.RoleRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.RoleResponse;
import com.ecommerce.store.service.model_service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request) {
       return ApiResponse
               .<RoleResponse>builder()
               .result(roleService.createRole(request))
               .build();
    }

    @PatchMapping("/{roleId}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable Integer roleId,
            @RequestBody @Valid RoleRequest request) {
        return ApiResponse
                .<RoleResponse>builder()
                .result(roleService.updateRole(roleId, request))
                .build();
    }

    @DeleteMapping("/{roleId}")
    public ApiResponse<Void> deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Role deleted successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> findAllRoles() {
        return ApiResponse
                .<List<RoleResponse>>builder()
                .result(roleService.findAllRoles())
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRoleByName(@PathVariable String name) {
        return ApiResponse
                .<RoleResponse>builder()
                .result(roleService.findRoleByName(name))
                .build();
    }
}

