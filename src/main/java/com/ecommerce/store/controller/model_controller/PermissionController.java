package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.PermissionRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.PermissionResponse;
import com.ecommerce.store.service.model_service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> addPermission(
            @RequestBody @Validated PermissionRequest permissionRequest
    ) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }

    @PatchMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> updatePermission(
            @RequestBody @Validated PermissionRequest permissionRequest,
            @PathVariable Integer permissionId
    ) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.updatePermission(permissionId, permissionRequest))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable Integer permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Permission deleted successfully")
                .build();
    }

    @GetMapping("/{permissionName}")
    public ApiResponse<PermissionResponse> getPermissionName(@PathVariable String permissionName) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.findByPermissionName(permissionName))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermission() {
        return ApiResponse
                .<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build();
    }
}
