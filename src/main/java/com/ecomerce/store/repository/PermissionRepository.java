package com.ecomerce.store.repository;

import com.ecomerce.store.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    boolean existsByPermissionNameIgnoreCase(String name);
    Optional<Permission> findByPermissionNameIgnoreCase(String name);
}
