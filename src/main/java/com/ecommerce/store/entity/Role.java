package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer roleId;

    @Column(nullable = false, unique = true, length = 40)
    String roleName;

    @Column(columnDefinition = "TEXT")
    String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RolePermission> rolePermissions;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<UserRole> userRoles;
}
