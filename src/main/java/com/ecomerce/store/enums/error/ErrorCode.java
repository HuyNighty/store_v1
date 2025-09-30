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
    PERMISSION_NOT_FOUND(1001, "Permission not found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1002, "User not existed", HttpStatus.BAD_REQUEST),
    USER_NAME_NOT_FOUND(1002, "Username not found", HttpStatus.NOT_FOUND),
    USER_NAME_EXISTED(1002, "Username existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1003, "Email existed", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1004, "Email not found", HttpStatus.NOT_FOUND),
    PHONE_NUMBER_NOT_FOUND(1005, "Phone number not found", HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH(1006, "Password mismatch", HttpStatus.BAD_REQUEST),;

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
