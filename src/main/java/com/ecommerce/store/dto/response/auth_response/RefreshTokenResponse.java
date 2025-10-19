package com.ecommerce.store.dto.response.auth_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenResponse {

    boolean success;
    String refreshToken;
    String accessToken;
}
