package com.ecomerce.store.service.auth_service;

import com.ecomerce.store.dto.request.auth_request.IntrospectRequest;
import com.ecomerce.store.dto.request.auth_request.RefreshTokenRequest;
import com.ecomerce.store.dto.response.auth_response.IntrospectResponse;
import com.ecomerce.store.dto.response.auth_response.RefreshTokenResponse;
import com.ecomerce.store.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface JwtService {

    String generateToken(User user);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    SignedJWT verifyToken(String token) throws ParseException, JOSEException;
    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
