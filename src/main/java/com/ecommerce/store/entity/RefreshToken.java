package com.ecommerce.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false, length = 2000)
    String token;

    @Column(nullable = false)
    LocalDateTime expiredAt;

    @Column(nullable = false)
    LocalDateTime createdAt;
}
