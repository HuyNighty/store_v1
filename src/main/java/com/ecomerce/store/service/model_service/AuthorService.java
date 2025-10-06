package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.AuthorRequest;
import com.ecomerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecomerce.store.dto.response.model_response.AuthorResponse;

import java.util.List;

public interface AuthorService {
    AuthorResponse create(AuthorRequest request);
    AuthorResponse update(Integer authorId, AuthorUpdateRequest request);
    void delete(Integer authorId);
    AuthorResponse getById(Integer authorId);
    List<AuthorResponse> getAll();
    List<AuthorResponse> searchByName(String keyword);
}
