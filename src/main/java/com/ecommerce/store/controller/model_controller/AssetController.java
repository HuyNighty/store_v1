package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.AssetRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.AssetResponse;
import com.ecommerce.store.service.model_service.AssetService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AssetController {


    AssetService assetService;

    @PostMapping
    public ApiResponse<AssetResponse> uploadAsset(@RequestBody @Valid AssetRequest request) {
        return ApiResponse
                .<AssetResponse>builder()
                .result(assetService.createAsset(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AssetResponse>> getAllAssets() {
        return ApiResponse
                .<List<AssetResponse>>builder()
                .result(assetService.findAll())
                .build();
    }

    @GetMapping("/{assetId}")
    public ApiResponse<AssetResponse> getAssetById(@PathVariable Integer assetId) {
        return ApiResponse
                .<AssetResponse>builder()
                .result(assetService.findById(assetId))
                .build();
    }

    @PatchMapping("/{assetId}")
    public ApiResponse<AssetResponse> updateAsset(
            @PathVariable Integer assetId,
            @RequestBody @Valid AssetRequest request) {
        return ApiResponse
                .<AssetResponse>builder()
                .result(assetService.updateAsset(assetId, request))
                .message("Asset updated successfully")
                .build();
    }

    @DeleteMapping("/{assetId}")
    public ApiResponse<Void> deleteAsset(@PathVariable Integer assetId) {
        assetService.softDelete(assetId);
        return ApiResponse
                .<Void>builder()
                .message("Asset deleted successfully")
                .build();
    }
}
