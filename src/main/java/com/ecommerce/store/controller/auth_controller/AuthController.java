package com.ecommerce.store.controller.auth_controller;

import com.ecommerce.store.dto.request.auth_request.*;
import com.ecommerce.store.dto.request.auth_request.*;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.auth_response.*;
import com.ecommerce.store.dto.response.auth_response.*;
import com.ecommerce.store.entity.InvalidatedToken;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.repository.InvalidatedTokenRepository;
import com.ecommerce.store.repository.RefreshTokenRepository;
import com.ecommerce.store.service.auth_service.AuthService;
import com.ecommerce.store.service.auth_service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;
    JwtService jwtService;

    RefreshTokenRepository refreshTokenRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody @Validated RegisterRequest registerRequest) {
        return ApiResponse
                .<RegisterResponse>builder()
                .result(authService.register(registerRequest))
                .build();
    }

    @PostMapping("/login")
    @Transactional
    public ApiResponse<LoginResponse> login(
            @RequestBody @Validated LoginRequest loginRequest,
            HttpServletResponse response) {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(authority -> log.info("Authorities: {}", authority.getAuthority()));

        LoginResponse login =  authService.login(loginRequest);

        String refreshToken = login.getRefreshToken();
        if (refreshToken != null) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofSeconds(jwtService.getRefreshDurationSeconds()))
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            login.setRefreshToken(null);
        }

        return ApiResponse
                .<LoginResponse>builder()
                .result(login)
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
    public ApiResponse<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody(required = false) LogoutRequest logoutRequest
    ) throws ParseException, JOSEException {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // true in prod with HTTPS
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
        }

        if (logoutRequest != null && logoutRequest.token() != null) {
            String bodyToken = logoutRequest.token();
            refreshTokenRepository.findByToken(bodyToken).ifPresent(refreshTokenRepository::delete);
            try {
                SignedJWT s = jwtService.verifyToken(bodyToken);
                String jti = s.getJWTClaimsSet().getJWTID();
                InvalidatedToken inv = InvalidatedToken.builder()
                        .jti(jti)
                        .expiredAt(s.getJWTClaimsSet().getExpirationTime())
                        .build();
                invalidatedTokenRepository.save(inv);
            } catch (Exception ex) {
            }
        }

        // 4) If Authorization header provided, blacklist access token jti
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String access = authHeader.substring(7);
            try {
                var signedJWT = jwtService.verifyToken(access);
                String jti = signedJWT.getJWTClaimsSet().getJWTID();
                Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

                InvalidatedToken invalidatedToken = InvalidatedToken
                        .builder()
                        .jti(jti)
                        .expiredAt(expiryTime)
                        .build();

                invalidatedTokenRepository.save(invalidatedToken);
            } catch (ParseException | JOSEException ignored) {
                // token invalid or expired — nothing to do
            }
        }

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout successfully!!!")
                .build();
    }


    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    )
            throws ParseException, JOSEException {
        if (refreshToken == null) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_MISSING);
        }
        RefreshTokenResponse refreshTokenResponse = jwtService.refreshToken(RefreshTokenRequest.builder().refreshToken(refreshToken).build());

        String newRefreshToken = refreshTokenResponse.getRefreshToken();

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofSeconds(jwtService.getRefreshDurationSeconds()))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LoginResponse out = new LoginResponse();
        out.setToken(refreshTokenResponse.getAccessToken());

        return ApiResponse
                .<LoginResponse>builder()
                .result(out)
                .build();
    }
}
