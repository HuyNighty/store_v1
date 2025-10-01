package com.ecomerce.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidated_tokens")
public class InvalidatedToken {

    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    String jti;

    @NotNull
    @Column(nullable = false)
    Date expiredAt;
}
