package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.ProductAssetRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import com.ecommerce.store.service.model_service.ProductAssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-assets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ProductAssetController {

    ProductAssetService productAssetService;

    @PostMapping
    public ApiResponse<ProductAssetResponse> addAssetToProduct(@RequestBody ProductAssetRequest request) {
        return ApiResponse
                .<ProductAssetResponse>builder()
                .result(productAssetService.addAssetToProduct(request))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> removeAssetFromProduct(@RequestBody ProductAssetRequest request) {
        productAssetService.removeAssetFromProduct(request);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Delete Successfully")
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<ProductAssetResponse>> getAssetsByProduct(@PathVariable Integer productId) {
        return ApiResponse
                .<List<ProductAssetResponse>>builder()
                .result( productAssetService.getAssetsByProduct(productId))
                .build();
    }

    @GetMapping("/type/{type}")
    public ApiResponse<List<ProductAssetResponse>> getAssetsByType(@PathVariable ProductAssetType type) {
        return ApiResponse
                .<List<ProductAssetResponse>>builder()
                .result(productAssetService.getAssetsByProductAssetType(type))
                .build();
    }

    @GetMapping("/ordinal/{ordinal}")
    public ApiResponse<List<ProductAssetResponse>> getAssetsByOrdinal(@PathVariable Integer ordinal) {
        return ApiResponse
                .<List<ProductAssetResponse>>builder()
                .result(productAssetService.getAssetsByOrdinal(ordinal))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductAssetResponse>> getAll() {
        return ApiResponse
                .<List<ProductAssetResponse>>builder()
                .result(productAssetService.getAll())
                .build();
    }
}

