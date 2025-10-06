package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.ProductAttributeValueRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.ProductAttributeValueResponse;
import com.ecommerce.store.entity.key.ProductAttributeValueId;
import com.ecommerce.store.service.model_service.ProductAttributeValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attribute-values")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class ProductAttributeValueController {

    ProductAttributeValueService productAttributeValueService;

    @PostMapping
    public ApiResponse<ProductAttributeValueResponse> create(@RequestBody ProductAttributeValueRequest request) {
        return ApiResponse
                .<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductAttributeValueResponse>> getAll() {
        return ApiResponse
                .<List<ProductAttributeValueResponse>>builder()
                .result(productAttributeValueService.getAll())
                .build();
    }

    @PatchMapping("/{productId}/{attributeId}")
    public ApiResponse<ProductAttributeValueResponse> update(
            @PathVariable Integer productId,
            @PathVariable Integer attributeId,
            @RequestBody ProductAttributeValueRequest request) {

        ProductAttributeValueId id = new ProductAttributeValueId(productId, attributeId);
        return ApiResponse
                .<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.update(id, request))
                .build();
    }

    @DeleteMapping("/{productId}/{attributeId}")
    public ApiResponse<Void> delete(@PathVariable Integer productId, @PathVariable Integer attributeId) {
        productAttributeValueService.delete(productId, attributeId);
        return ApiResponse
                .<Void>builder()
                .message("ProductAttributeValue deleted successfully")
                .build();
    }

    @GetMapping("/{productId}/{attributeId}")
    public ApiResponse<ProductAttributeValueResponse> getById(@PathVariable Integer productId, @PathVariable Integer attributeId) {
        return ApiResponse
                .<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.getById(productId, attributeId))
                .build();
    }
}
