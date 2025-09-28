package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    UUID userId;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 4, max = 30)
    String userName;

    @Column(nullable = false)
    @NotNull
    @Size(min = 8)
    String password;

    @Column(nullable = false, unique = true)
    @Email
    @NotNull
    String email;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @NotNull
    Boolean enabled = true;

    LocalDateTime lastLoginAt;

    @Version
    @NotNull
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
