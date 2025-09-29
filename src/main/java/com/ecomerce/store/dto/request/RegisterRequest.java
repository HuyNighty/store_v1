package com.ecomerce.store.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(min = 4, max = 30)
        String userName,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String phoneNumber,

        @NotBlank
        String address
) {
}
