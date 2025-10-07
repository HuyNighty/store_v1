package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.CategoryRequest;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Integer id, CategoryRequest request);
    CategoryResponse getById(Integer id);
    List<CategoryResponse> getAll();
    void softDelete(Integer id);
}
