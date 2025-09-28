package com.ecomerce.store.repository;

import com.ecomerce.store.entity.ProductCategory;
import com.ecomerce.store.entity.key.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
