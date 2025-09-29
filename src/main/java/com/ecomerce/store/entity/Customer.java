package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    String customerId;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false, length = 30)
    String phoneNumber;

    @Column(nullable = false)
    String address;

    Integer loyaltyPoints = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    User user;
}
