package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.BookAuthorRequest;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.key.BookAuthorId;

import java.util.List;

public interface BookAuthorService {

    BookAuthorResponse create(BookAuthorRequest request);

    BookAuthorResponse getById(Integer productId, Integer authorId);

    List<BookAuthorResponse> getAll();

    List<BookAuthorResponse> getByAuthorNameContaining(String keyword);

    List<BookAuthorResponse> getByProductId(Integer productId);

    BookAuthorResponse update(BookAuthorId id, BookAuthorRequest request);

    void delete(Integer productId, Integer authorId);
}
