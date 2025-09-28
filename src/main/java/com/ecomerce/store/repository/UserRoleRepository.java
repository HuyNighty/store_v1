package com.ecomerce.store.repository;

import com.ecomerce.store.entity.UserRole;
import com.ecomerce.store.entity.key.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
