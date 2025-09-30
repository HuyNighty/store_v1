package com.ecomerce.store.dto.response.auth_response;

public record RegisterResponse(
        String userId,
        String userName,
        String email,
        String fullName,
        String phoneNumber,
        String address
) {
}
