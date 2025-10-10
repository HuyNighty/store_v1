package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.CategoryRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.service.model_service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.create(request))
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<CategoryResponse> update(
            @PathVariable Integer id,
            @RequestBody CategoryRequest request) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.update(id, request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Integer id) {
        return ApiResponse
                .<CategoryResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        return ApiResponse
                .<List<CategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

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
