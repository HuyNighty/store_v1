package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseSoftDeleteEntity;
import com.ecomerce.store.enums.entity_enums.CouponEnums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer couponId;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    DiscountType discountType;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    BigDecimal discountValue;

    @DecimalMin("0.0")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    BigDecimal minOrderAmount = BigDecimal.ZERO;

    Integer usageLimit;

    LocalDate validFrom;
    LocalDate validTo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean isActive = true;

    public boolean isValidNow() {
        LocalDate today = LocalDate.now();
        return Boolean.TRUE.equals(isActive)
                && (validFrom == null || !today.isBefore(validFrom))
                && (validTo == null || !today.isAfter(validTo));
    }
}
