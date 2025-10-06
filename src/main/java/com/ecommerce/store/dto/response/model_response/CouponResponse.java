package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.CouponEnums.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CouponResponse(
        Integer couponId,
        String code,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderAmount,
        Integer usageLimit,
        LocalDate validFrom,
        LocalDate validTo,
        Boolean isActive,
        Boolean deleted
) {}
