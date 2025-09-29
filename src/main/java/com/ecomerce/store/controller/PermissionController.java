package com.ecomerce.store.controller;

import com.ecomerce.store.dto.request.PermissionRequest;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.PermissionResponse;
import com.ecomerce.store.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> addPermission(
            @RequestBody @Validated PermissionRequest permissionRequest
    ) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @PutMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> updatePermission(
            @RequestBody @Validated PermissionRequest permissionRequest,
            @PathVariable Integer permissionId
    ) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.update(permissionId, permissionRequest))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable Integer permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Permission deleted successfully")
                .build();
    }

    @GetMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> getPermission(@PathVariable Integer permissionId) {
        return ApiResponse
                .<PermissionResponse>builder()
                .result(permissionService.findById(permissionId))
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
