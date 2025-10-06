package com.ecommerce.store.repository;

import com.ecommerce.store.entity.UserRole;
import com.ecommerce.store.entity.key.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
