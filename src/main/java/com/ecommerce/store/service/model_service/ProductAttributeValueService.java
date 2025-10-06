package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.ProductAttributeValueRequest;
import com.ecommerce.store.dto.response.model_response.ProductAttributeValueResponse;
import com.ecommerce.store.entity.key.ProductAttributeValueId;

import java.util.List;

public interface ProductAttributeValueService {

    ProductAttributeValueResponse create(ProductAttributeValueRequest request);

    ProductAttributeValueResponse getById(Integer productId, Integer attributeId);

    List<ProductAttributeValueResponse> getAll();

    ProductAttributeValueResponse update(ProductAttributeValueId id, ProductAttributeValueRequest request);

    void delete(Integer productId, Integer attributeId);
}

