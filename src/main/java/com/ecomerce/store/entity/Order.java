package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import com.ecomerce.store.enums.entity_enums.OrderEnums.PaymentMethod;
import com.ecomerce.store.enums.entity_enums.OrderEnums.StatusOrder;
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

    @NotNull
    @Column(nullable = false, unique = true)
    String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    StatusOrder statusOrder;

    @Column(nullable = false)
    @NotNull
    @Positive
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  length = 20)
    @NotNull
    PaymentMethod paymentMethod;

    @Size(max = 2000)
    @Column(columnDefinition = "TEXT")
    @Size(max = 2000)
    String note;

    @Column(nullable =  false)
    @NotNull
    @Size(min = 5, max = 255)
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
