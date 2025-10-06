package com.ecommerce.store.exception;

import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.enums.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        if (exception.getErrors() != null && !exception.getErrors().isEmpty()) {
            apiResponse.setErrors(exception.getErrors());
        }

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
