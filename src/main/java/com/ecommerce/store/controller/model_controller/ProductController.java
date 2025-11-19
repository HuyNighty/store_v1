package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.service.model_service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        return ApiResponse
                .<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Integer productId) {
        return ApiResponse
                .<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse
                .<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/public/{categoryName}")
    public ApiResponse<List<ProductResponse>> getAllProductsByCategoryName(@PathVariable String categoryName) {
        return ApiResponse
                .<List<ProductResponse>>builder()
                .result(productService.getProductsByCategoryName(categoryName))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequest request) {
        return ApiResponse
                .<ProductResponse>builder()
                .result(productService.updateProduct(productId, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Delete Product successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchProducts(@RequestParam("keyword") String keyword) {
        return ApiResponse
                .<List<ProductResponse>>builder()
                .result(productService.searchProducts(keyword))
                .build();
    }

    @GetMapping("/by-category/{categoryId}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable Integer categoryId) {
        return ApiResponse
                .<List<ProductResponse>>builder()
                .result(productService.getProductsByCategory(categoryId))
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse<List<ProductResponse>> filterProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.filterProducts(categoryId, minPrice, maxPrice, minRating))
                .build();
    }
}
