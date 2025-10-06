package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.CouponRequest;
import com.ecommerce.store.dto.response.model_response.CouponResponse;
import com.ecommerce.store.entity.Coupon;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    Coupon toEntity(CouponRequest couponRequest);

    CouponResponse toResponse(Coupon coupon);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCouponFromRequest(CouponRequest request, @MappingTarget Coupon coupon);
}
