package com.ecomerce.store.dto.request.model_request;

public record CustomerRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Integer loyaltyPoints
) {}

