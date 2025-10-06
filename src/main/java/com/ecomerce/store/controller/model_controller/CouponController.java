package com.ecomerce.store.controller.model_controller;

import com.ecomerce.store.dto.request.model_request.CouponRequest;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.model_response.CouponResponse;
import com.ecomerce.store.service.model_service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class CouponController {

    CouponService couponService;

    @PostMapping
    public ApiResponse<CouponResponse> create(@RequestBody CouponRequest request) {
        return ApiResponse
                .<CouponResponse>builder()
                .result(couponService.createCoupon(request))
                .build();
    }

    @PatchMapping("/{couponId}")
    public ApiResponse<CouponResponse> update(@PathVariable Integer couponId,
                                              @RequestBody CouponRequest request) {
        return ApiResponse
                .<CouponResponse>builder()
                .result(couponService.updateCoupon(couponId, request))
                .build();
    }

    @GetMapping("/{couponId}")
    public ApiResponse<CouponResponse> getById(@PathVariable Integer couponId) {
        return ApiResponse
                .<CouponResponse>builder()
                .result(couponService.findById(couponId))
                .build();
    }

    @GetMapping("/code/{code}")
    public ApiResponse<CouponResponse> getByCode(@PathVariable String code) {
        return ApiResponse
                .<CouponResponse>builder()
                .result(couponService.findByCode(code))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CouponResponse>> getAll() {
        return ApiResponse
                .<List<CouponResponse>>builder()
                .result(couponService.findAll())
                .build();
    }

    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> softDelete(@PathVariable Integer couponId) {
        couponService.softDelete(couponId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Coupon deleted successfully!")
                .build();
    }
}
