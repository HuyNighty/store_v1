package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.ProductCategoryRequest;
import com.ecommerce.store.dto.response.model_response.ProductCategoryResponse;

import java.util.List;

public interface ProductCategoryService {
    ProductCategoryResponse assignProductToCategory(ProductCategoryRequest request);
    void removeProductFromCategory(Integer productId, Integer categoryId);
    List<ProductCategoryResponse> getAll();
}
