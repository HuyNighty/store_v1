package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.AttributeRequest;
import com.ecommerce.store.dto.response.model_response.AttributeResponse;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.service.model_service.AttributeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class AttributeController {

    AttributeService attributeService;

    @PostMapping
    public ApiResponse<AttributeResponse> create(@Valid @RequestBody AttributeRequest request) {
        return ApiResponse
                .<AttributeResponse>builder()
                .result(attributeService.create(request))
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<AttributeResponse> update(@PathVariable Integer id,
                                                 @Valid @RequestBody AttributeRequest request) {
        return ApiResponse
                .<AttributeResponse>builder()
                .result(attributeService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        attributeService.delete(id);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Delete attribute successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AttributeResponse> getById(@PathVariable Integer id) {
        return ApiResponse
                .<AttributeResponse>builder()
                .result(attributeService.getById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AttributeResponse>> getAll() {
        return ApiResponse
                .<List<AttributeResponse>>builder()
                .result(attributeService.getAll())
                .build();
    }

    @GetMapping("/search/{keyword}")
    public ApiResponse<List<AttributeResponse>> searchByName(@PathVariable String keyword) {
        return ApiResponse
                .<List<AttributeResponse>>builder()
                .result(attributeService.getByNameContaining(keyword))
                .build();
    }
}

