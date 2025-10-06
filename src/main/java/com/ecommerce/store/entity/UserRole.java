package com.ecommerce.store.entity;

import com.ecommerce.store.entity.key.UserRoleId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    UserRoleId userRoleId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",  nullable = false, columnDefinition = "VARCHAR(36)")
    User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    Role role;
}
