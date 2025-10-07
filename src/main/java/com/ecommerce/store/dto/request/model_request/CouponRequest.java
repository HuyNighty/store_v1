package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.CouponEnums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CouponRequest(

        @NotBlank(message = "Coupon code is required")
        String code,

        @NotNull(message = "Discount type is required")
        DiscountType discountType,

        @NotNull(message = "Discount value is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
        BigDecimal discountValue,

        @DecimalMin(value = "0.0", message = "Minimum order amount must be >= 0")
        BigDecimal minOrderAmount,

        @PositiveOrZero(message = "Usage limit must be >= 0")
        Integer usageLimit,

        @PastOrPresent(message = "Valid from cannot be in the future")
        LocalDate validFrom,

        @FutureOrPresent(message = "Valid to must be today or in the future")
        LocalDate validTo,

        Boolean isActive
) {}
