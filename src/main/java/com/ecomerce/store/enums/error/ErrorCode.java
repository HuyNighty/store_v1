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
    PRODUCT_SKU_EXISTED(1015, "Product sku existed", HttpStatus.BAD_REQUEST),
    PRODUCT_SLUG_EXISTED(1016, "Product slug existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1017, "Product not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(1018, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1019, "Cart item not found", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(1020, "Review not found", HttpStatus.NOT_FOUND),
    FORBIDDEN(1021, "Forbidden", HttpStatus.FORBIDDEN),
    INVALID_RATING(1022, "Invalid rating", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1023, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(1024, "Order item not found", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY(1025, "Invalid quantity", HttpStatus.BAD_REQUEST),
    CART_EMPTY(1026, "Cart empty", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION(1027, "Invalid status transition", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_FINALIZED(1028, "Order already finalized", HttpStatus.BAD_REQUEST),
    CANCELED_INVALID(1028, "Can not cancel", HttpStatus.BAD_REQUEST),
    URL_EXISTED(1029, "Url existed", HttpStatus.BAD_REQUEST),
    ASSET_NOT_FOUND(1030, "Asset not found", HttpStatus.NOT_FOUND),
    ASSET_DELETED(1031, "Asset deleted", HttpStatus.BAD_REQUEST),
    PRODUCT_ASSET_NOT_FOUND(1032, "Product asset not found", HttpStatus.NOT_FOUND),
    PRODUCT_ASSET_EXISTED(1033, "Product asset existed", HttpStatus.BAD_REQUEST),
    COUPON_CODE_EXISTED(1034, "Coupon code existed", HttpStatus.BAD_REQUEST),
    COUPON_NOT_FOUND(1035, "Coupon not found", HttpStatus.NOT_FOUND),
    COUPON_ALREADY_DELETED(1036, "Coupon already deleted", HttpStatus.BAD_REQUEST),
    AUTHOR_EXISTED(1037, "Author existed", HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_FOUND(1038, "Author not found", HttpStatus.NOT_FOUND),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
