package com.ecomerce.store.service.auth_service;

import com.ecomerce.store.dto.request.auth_request.LoginRequest;
import com.ecomerce.store.dto.request.auth_request.RegisterRequest;
import com.ecomerce.store.dto.response.auth_response.LoginResponse;
import com.ecomerce.store.dto.response.auth_response.RegisterResponse;
import com.ecomerce.store.dto.response.auth_response.UserInfoResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserInfoResponse userInfo();
}
