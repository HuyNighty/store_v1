package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderItemId;

    @NotNull
    @Column(nullable = false)
    @Positive
    Integer quantity;

    @Positive
    @Column(nullable = false)
    @NotNull
    BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
