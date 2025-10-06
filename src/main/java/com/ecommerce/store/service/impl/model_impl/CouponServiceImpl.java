package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.CouponRequest;
import com.ecommerce.store.dto.response.model_response.CouponResponse;
import com.ecommerce.store.entity.Coupon;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.CouponMapper;
import com.ecommerce.store.repository.CouponRepository;
import com.ecommerce.store.service.model_service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class CouponServiceImpl implements CouponService {

    CouponRepository couponRepository;
    CouponMapper couponMapper;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.code())) {
            throw new AppException(ErrorCode.COUPON_CODE_EXISTED);
        }

        Coupon coupon = couponMapper.toEntity(request);
        coupon.setCreatedAt(LocalDateTime.now());
        couponRepository.save(coupon);

        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse updateCoupon(Integer couponId, CouponRequest request) {
        Coupon coupon = getActiveCoupon(couponId);

        if (request.code() != null && !request.code().isEmpty()) {
            boolean exists = couponRepository.existsByCode(request.code());
            if (exists && !coupon.getCode().equals(request.code())) {
                throw new AppException(ErrorCode.COUPON_CODE_EXISTED);
            }
            coupon.setCode(request.code());
        }

        couponMapper.updateCouponFromRequest(request, coupon);
        coupon.setUpdatedAt(LocalDateTime.now());
        couponRepository.save(coupon);

        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse findById(Integer couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        return couponMapper.toResponse(coupon);
    }

    @Override
    public List<CouponResponse> findAll() {
        return couponRepository.findAll()
                .stream()
                .filter(coupon -> coupon.getIsActive() == true && coupon.getDeletedAt() == null)
                .map(couponMapper::toResponse)
                .toList();
    }

    @Override
    public void softDelete(Integer couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));

        coupon.setDeletedAt(LocalDateTime.now());
        coupon.setIsActive(false);
        couponRepository.save(coupon);
    }

    @Override
    public CouponResponse findByCode(String code) {
        Coupon coupon = couponRepository.findByCodeAndDeletedAtIsNull(code)
                .filter(c -> c.getIsActive() == true && c.getDeletedAt() == null)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        return couponMapper.toResponse(coupon);
    }

    private Coupon getActiveCoupon(Integer couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));

        if (coupon.getIsActive() != true) {
            throw new AppException(ErrorCode.COUPON_ALREADY_DELETED);
        }

        return coupon;
    }
}
