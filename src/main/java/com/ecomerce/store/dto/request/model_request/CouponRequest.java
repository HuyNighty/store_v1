package com.ecomerce.store.dto.request.model_request;

import com.ecomerce.store.enums.entity_enums.CouponEnums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CouponRequest(

        @NotBlank
        String code,

        @NotNull
        DiscountType discountType,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal discountValue,

        @DecimalMin("0.0")
        BigDecimal minOrderAmount,

        Integer usageLimit,

        LocalDate validFrom,

        LocalDate validTo,

        Boolean isActive
) {}
