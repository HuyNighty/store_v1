package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productAssets pa " +
            "LEFT JOIN FETCH pa.asset " +
            "LEFT JOIN FETCH p.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE p.productId = :productId AND p.isActive = true")
    Optional<Product> findActiveProductWithDetails(@Param("productId") Integer productId);

    Optional<Product> findByProductIdAndIsActiveTrue(Integer productId);
}
