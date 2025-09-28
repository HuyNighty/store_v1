package com.ecomerce.store.entity;

import com.ecomerce.store.entity.key.UserRoleId;
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
    @JoinColumn(name = "user_id",  nullable = false)
    User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    Role role;
}
