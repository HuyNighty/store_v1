package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.CouponRequest;
import com.ecommerce.store.dto.response.model_response.CouponResponse;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest request);

    CouponResponse updateCoupon(Integer couponId, CouponRequest request);

    CouponResponse findById(Integer couponId);

    List<CouponResponse> findAll();

    void softDelete(Integer couponId);

    CouponResponse findByCode(String code);

}
