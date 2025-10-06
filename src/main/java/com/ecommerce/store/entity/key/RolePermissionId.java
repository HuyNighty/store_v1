package com.ecommerce.store.entity.key;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionId implements Serializable {

    Integer roleId;
    Integer permissionId;
}
