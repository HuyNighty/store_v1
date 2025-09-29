package com.ecomerce.store.enums.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    PERMISSION_EXISTED(1000, "Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1001, "Permission not found", HttpStatus.NOT_FOUND),;

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
