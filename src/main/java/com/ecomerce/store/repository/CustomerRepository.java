package com.ecomerce.store.repository;

import com.ecomerce.store.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByUser_UserId(String userId);
    boolean existsByPhoneNumber(String phoneNumber);
}
