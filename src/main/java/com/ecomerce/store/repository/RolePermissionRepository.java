package com.ecomerce.store.repository;

import com.ecomerce.store.entity.RolePermission;
import com.ecomerce.store.entity.key.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    List<RolePermission> findByRoleRoleId(Integer roleId);
}
