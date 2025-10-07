package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import com.ecommerce.store.enums.entity_enums.OrderEnums.PaymentMethod;
import com.ecommerce.store.enums.entity_enums.OrderEnums.StatusOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderId;

    @Column(nullable = false, unique = true)
    String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    StatusOrder statusOrder;

    @Column(nullable = false)
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  length = 20)
    PaymentMethod paymentMethod;

    @Column(columnDefinition = "TEXT")
    String note;

    @Column(nullable =  false)
    String shippingAddress;

    BigDecimal shippingCost;

    LocalDateTime canceledAt;

    LocalDateTime completedAt;

    LocalDateTime deliveredAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "order", cascade =  CascadeType.ALL, orphanRemoval = true)
    Set<OrderItem> orderItems;
}
