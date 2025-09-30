package com.ecomerce.store.dto.response;

public record RegisterResponse(
        String userId,
        String userName,
        String email,
        String fullName,
        String phoneNumber,
        String address
) {
}
