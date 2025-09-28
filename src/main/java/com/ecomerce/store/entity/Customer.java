package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    UUID customerId;

    @NotNull
    @Column(nullable = false)
    String firstName;

    @NotNull
    @Column(nullable = false)
    String lastName;

    @NotNull
    @Column(nullable = false, length = 30)
    String phoneNumber;

    @NotNull
    @Column(nullable = false)
    String address;

    Integer loyaltyPoints = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    User user;
}
