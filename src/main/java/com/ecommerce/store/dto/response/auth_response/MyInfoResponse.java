package com.ecommerce.store.dto.response.auth_response;

import lombok.Builder;

@Builder
public record MyInfoResponse(
        String userName,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String address,
        Integer loyaltyPoints
) {
}
