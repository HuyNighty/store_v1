package com.ecommerce.store.service.auth_service;

import com.ecommerce.store.dto.request.auth_request.IntrospectRequest;
import com.ecommerce.store.dto.request.auth_request.RefreshTokenRequest;
import com.ecommerce.store.dto.response.auth_response.IntrospectResponse;
import com.ecommerce.store.dto.response.auth_response.RefreshTokenResponse;
import com.ecommerce.store.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface JwtService {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    SignedJWT verifyToken(String token) throws ParseException, JOSEException;
    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
    Long getRefreshDurationSeconds();
}
