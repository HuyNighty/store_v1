package com.ecommerce.store.repository;

import com.ecommerce.store.entity.RolePermission;
import com.ecommerce.store.entity.key.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    List<RolePermission> findByRoleRoleId(Integer roleId);
}
