package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.request.model_request.FullBookUpdateRequest;
import com.ecommerce.store.dto.response.model_response.FullBookResponse;

public interface FullBookService {
    FullBookResponse createFullBook(FullBookRequest request);
    FullBookResponse updateFullBook(Integer productId, FullBookUpdateRequest request);


}
