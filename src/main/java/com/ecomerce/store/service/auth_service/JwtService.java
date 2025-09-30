package com.ecomerce.store.service.auth_service;

import com.ecomerce.store.dto.request.auth_request.IntrospectRequest;
import com.ecomerce.store.dto.response.auth_response.IntrospectResponse;
import com.ecomerce.store.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtService {

    String generateToken(User user);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

}
