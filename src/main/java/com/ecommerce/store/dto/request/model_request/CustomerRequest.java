package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CustomerRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Invalid phone number format")
        String phoneNumber,

        @NotBlank(message = "Address is required")
        @Size
        String address,

        @PositiveOrZero(message = "Loyalty points cannot be negative")
        Integer loyaltyPoints
) {}

