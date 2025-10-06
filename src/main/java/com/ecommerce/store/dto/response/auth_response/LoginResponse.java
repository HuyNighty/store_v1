package com.ecommerce.store.dto.response.auth_response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String userId;
    String userName;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    LocalDateTime lastLoginAt;
    Integer loyaltyPoints;
    String token;
    Set<String> roles;
}
