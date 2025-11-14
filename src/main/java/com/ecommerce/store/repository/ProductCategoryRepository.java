package com.ecommerce.store.repository;

import com.ecommerce.store.entity.ProductCategory;
import com.ecommerce.store.entity.key.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    List<ProductCategory> findByCategoryCategoryId(Integer categoryId);

    List<ProductCategory> findByProductProductId(Integer productId);

    @Query("SELECT pc FROM ProductCategory pc JOIN FETCH pc.product p JOIN FETCH pc.category c WHERE c.categoryId = :categoryId AND p.isActive = true")
    List<ProductCategory> findActiveProductsByCategoryId(@Param("categoryId") Integer categoryId);

    List<ProductCategory> findByProduct_ProductId(Integer productId);

}
