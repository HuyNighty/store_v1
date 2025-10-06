package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByOrderIdAndUserUserId(Integer orderId, String userId);
    List<Order> findByUserUserId(String userId);
}
