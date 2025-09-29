package com.ecomerce.store.repository;

import com.ecomerce.store.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    boolean existsByPermissionName(String name);
}
