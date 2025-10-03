package com.ecomerce.store.repository;

import com.ecomerce.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
