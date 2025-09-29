package com.ecomerce.store.repository;

import com.ecomerce.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
