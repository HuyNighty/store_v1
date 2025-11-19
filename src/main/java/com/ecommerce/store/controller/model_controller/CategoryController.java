package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.CategoryRequest;
import com.ecommerce.store.dto.request.model_request.CategoryUpdateRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.service.model_service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/public")
    public ApiResponse<List<CategoryResponse>> getAllPublic() {
        List<CategoryResponse> activeCategories = categoryService.getAll()
                .stream()
                .filter(category -> Boolean.TRUE.equals(category.isActive()))
                .collect(Collectors.toList());

        return ApiResponse
                .<List<CategoryResponse>>builder()
                .result(activeCategories)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CategoryResponse>> getAllAdmin() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Integer id) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.create(request))
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> update(
            @PathVariable Integer id,
            @RequestBody CategoryUpdateRequest request) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.update(id, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Integer id) {
        categoryService.softDelete(id);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Category has been deactivated successfully.")
                .build();
    }
}