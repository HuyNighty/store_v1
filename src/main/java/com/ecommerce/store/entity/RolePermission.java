package com.ecommerce.store.entity;

import com.ecommerce.store.entity.key.RolePermissionId;
import jakarta.persistence.*;
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
@Table(name = "role_permissions")
public class RolePermission {

    @EmbeddedId
    RolePermissionId rolePermissionId;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    Permission permission;

    LocalDateTime assignedAt;
}
