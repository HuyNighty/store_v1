package com.ecommerce.store.service.auth_service;

import com.ecommerce.store.dto.request.auth_request.LoginRequest;
import com.ecommerce.store.dto.request.auth_request.LogoutRequest;
import com.ecommerce.store.dto.request.auth_request.RegisterRequest;
import com.ecommerce.store.dto.response.auth_response.LoginResponse;
import com.ecommerce.store.dto.response.auth_response.RegisterResponse;
import com.ecommerce.store.dto.response.auth_response.UserInfoResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.Set;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserInfoResponse userInfo();
    Set<UserInfoResponse> getAllUsers();
    void logout(LogoutRequest request) throws ParseException, JOSEException;
 }
