package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.RolePermissionRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.RolePermissionResponse;
import com.ecommerce.store.service.model_service.RolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RolePermissionController {

    RolePermissionService rolePermissionService;

    @PostMapping("/assign")
    public ApiResponse<RolePermissionResponse> assignPermission(
            @RequestBody RolePermissionRequest request) {
        return ApiResponse.<RolePermissionResponse>builder()
                .result(rolePermissionService.assignPermission(request))
                .build();
    }

    @DeleteMapping("/remove")
    public ApiResponse<Void> removePermission(
            @RequestBody RolePermissionRequest request) {
        rolePermissionService.removePermission(request);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Permission removed")
                .build();
    }

    @GetMapping("/{roleId}")
    public ApiResponse<List<RolePermissionResponse>> getPermissionsByRole(
            @PathVariable Integer roleId) {
        return ApiResponse.<List<RolePermissionResponse>>builder()
                .result(rolePermissionService.getPermissionsByRole(roleId))
                .build();
    }
}
