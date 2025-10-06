package com.ecommerce.store.repository;

import com.ecommerce.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
}
