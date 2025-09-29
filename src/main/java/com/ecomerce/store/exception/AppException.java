package com.ecomerce.store.exception;

import com.ecomerce.store.enums.error.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
