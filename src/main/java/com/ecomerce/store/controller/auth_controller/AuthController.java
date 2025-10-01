package com.ecomerce.store.controller.auth_controller;

import com.ecomerce.store.dto.request.auth_request.*;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.auth_response.*;
import com.ecomerce.store.service.auth_service.AuthService;
import com.ecomerce.store.service.auth_service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;
    JwtService jwtService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody @Validated RegisterRequest registerRequest) {
        return ApiResponse
                .<RegisterResponse>builder()
                .result(authService.register(registerRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(authority -> log.info("Authorities: {}", authority.getAuthority()));

        return ApiResponse
                .<LoginResponse>builder()
                .result(authService.login(loginRequest))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        return ApiResponse
                .<IntrospectResponse>builder()
                .result(jwtService.introspect(introspectRequest))
                .build();
    }

    @GetMapping("/user-info")
    public ApiResponse<UserInfoResponse> userInfo() {
        return ApiResponse
                .<UserInfoResponse>builder()
                .result(authService.userInfo())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ApiResponse<Set<UserInfoResponse>> getAllUser() {
        return ApiResponse
                .<Set<UserInfoResponse>>builder()
                .result(authService.getAllUsers())
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Logout successfully!")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {
        return ApiResponse
                .<RefreshTokenResponse>builder()
                .result(jwtService.refreshToken(refreshTokenRequest))
                .build();
    }
}
