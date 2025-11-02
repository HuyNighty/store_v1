package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUserUserId(String userId);
    List<Review> findByProductProductId(Integer productId);
    List<Review> findByProductProductIdAndIsApprovedTrue(Integer productId);
}
