package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer reviewId;

    @Min(0)
    @Max(10)
    @Positive
    @NotNull
    @Column(nullable = false)
    @Size(max = 10)
    Float rating;

    @Column(columnDefinition = "TEXT")
    String comment;

    @NotNull
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean isApproved = false;

    LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
