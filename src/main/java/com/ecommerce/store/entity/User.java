package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    String userId;

    @Column(nullable = false, unique = true)
    String userName;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    Boolean enabled = true;

    LocalDateTime lastLoginAt;

    @Version
    Integer version;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Cart cart;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    Set<Order> orders;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;
}
