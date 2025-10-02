package com.ecomerce.store.dto.response.model_response;

public record CustomerResponse(
        String customerId,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Integer loyaltyPoints
) {}

