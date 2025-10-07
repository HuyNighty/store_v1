package com.ecommerce.store.dto.request.auth_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "user name is required")
        @Size(min = 4, max = 30)
        String userName,

        @Email(message = "email cannot be null")
        @NotBlank(message = "email is required")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 8)
        String password,

        String firstName,

        String lastName,

        @NotBlank(message = "phone number is required")
        String phoneNumber,

        @NotBlank(message = "address is required")
        String address
) {}
