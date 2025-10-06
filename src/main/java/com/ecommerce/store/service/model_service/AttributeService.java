package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.AttributeRequest;
import com.ecommerce.store.dto.response.model_response.AttributeResponse;

import java.util.List;

public interface AttributeService {

    AttributeResponse create(AttributeRequest request);

    AttributeResponse update(Integer id, AttributeRequest request);

    void delete(Integer id);

    AttributeResponse getById(Integer id);

    List<AttributeResponse> getAll();

    List<AttributeResponse> getByNameContaining(String keyword);
}
