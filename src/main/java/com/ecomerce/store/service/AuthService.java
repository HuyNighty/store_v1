package com.ecomerce.store.service;

import com.ecomerce.store.dto.request.RegisterRequest;
import com.ecomerce.store.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse toResponse(RegisterRequest request);
}
