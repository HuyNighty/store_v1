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
    PASSWORD_MISMATCH(1006, "Password mismatch", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1007, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTED(1011, "Role already existed", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(1010, "Permission denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ROLE_PERMISSION_NOT_FOUND(1012, "Role permission not found", HttpStatus.NOT_FOUND),
    ROLE_PERMISSION_ALREADY_EXISTED(1013, "Role permission already existed", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTED(1014, "Phone already existed", HttpStatus.BAD_REQUEST),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
