package com.ecomerce.store.controller.model_controller;

import com.ecomerce.store.dto.request.model_request.ProductRequest;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.model_response.ProductResponse;
import com.ecomerce.store.service.model_service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Integer productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete Product successfully")
                .build();
    }
}
