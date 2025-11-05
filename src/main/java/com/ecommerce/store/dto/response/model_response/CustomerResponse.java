package com.ecommerce.store.dto.response.model_response;

public record CustomerResponse(
        String customerId,
        String profileImage,
        String userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Integer loyaltyPoints,
        String email,
        Double totalOrders,
        Double totalSpent
) {}

