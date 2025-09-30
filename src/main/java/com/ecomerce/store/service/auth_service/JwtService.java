package com.ecomerce.store.service.auth_service;

import com.ecomerce.store.entity.User;

public interface JwtService {

    String generateToken(User user);
}
