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
@Table(name = "permissions")
public class Permission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer permissionId;

    @Column(nullable = false, length = 40, unique = true)
    String permissionName;

    @Column(columnDefinition = "TEXT")
    String description;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RolePermission> rolePermissions;
}
