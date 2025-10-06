package com.ecommerce.store.exception;

import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.enums.error.ErrorCodeDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
    List<ErrorCodeDetail> errors;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, List<ErrorCodeDetail> errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
