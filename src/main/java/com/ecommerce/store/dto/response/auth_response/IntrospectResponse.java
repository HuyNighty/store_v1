package com.ecommerce.store.dto.response.auth_response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {
    boolean verified;
}
