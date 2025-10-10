package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.ProductCategoryRequest;
import com.ecommerce.store.dto.response.model_response.ProductCategoryResponse;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.service.model_service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class ProductCategoryController {

    ProductCategoryService productCategoryService;

    @PostMapping
    public ApiResponse<ProductCategoryResponse> assignProductToCategory(
            @RequestBody @Valid ProductCategoryRequest request) {
        return ApiResponse
                .<ProductCategoryResponse>builder()
                .result(productCategoryService.assignProductToCategory(request))
                .build();
    }

        @DeleteMapping("/products/{productId}/categories/{categoryId}")
    public ApiResponse<Void> removeProductFromCategory(
            @PathVariable Integer productId,
            @PathVariable Integer categoryId) {
        productCategoryService.removeProductFromCategory(productId, categoryId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Removed product from category successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductCategoryResponse>> getAll() {
        return ApiResponse
                .<List<ProductCategoryResponse>>builder()
                .result(productCategoryService.getAll())
                .build();
    }
}
