package com.ecomerce.store.service;

import com.ecomerce.store.entity.User;

public interface JwtService {

    String generateToken(User user);
}
