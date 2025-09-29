package com.ecomerce.store.dto.response;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String userName,
        String email,
        String fullName,
        String phoneNumber,
        String address
) {
}
