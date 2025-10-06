package com.ecommerce.store.repository;

import com.ecommerce.store.entity.ProductCategory;
import com.ecommerce.store.entity.key.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
