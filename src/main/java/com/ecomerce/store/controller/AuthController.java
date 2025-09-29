package com.ecomerce.store.controller;

import com.ecomerce.store.dto.request.RegisterRequest;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.RegisterResponse;
import com.ecomerce.store.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ApiResponse
                .<RegisterResponse>builder()
                .result(authService.toResponse(registerRequest))
                .build();
    }
}
