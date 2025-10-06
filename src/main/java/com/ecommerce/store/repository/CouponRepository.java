package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    boolean existsByCode(String code);
    Optional<Coupon> findByCodeAndDeletedAtIsNull(String code);
}
