package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.productAssets pa " +
            "JOIN p.productCategory pc " +
            "WHERE LOWER(pc.category.categoryName) LIKE CONCAT('%', LOWER(:categoryName), '%')")
    List<Product> findWithAssetsByCategoryNameContainingIgnoreCase(@Param("categoryName") String categoryName);


    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productAssets pa " +
            "LEFT JOIN FETCH pa.asset " +
            "LEFT JOIN FETCH p.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE p.productId = :productId AND p.isActive = true")
    Optional<Product> findActiveProductWithDetails(@Param("productId") Integer productId);

    @Query("SELECT p FROM Product p JOIN p.bookAuthors ba WHERE ba.author.authorId = :authorId AND p.isActive = true")
    List<Product> findByAuthorId(@Param("authorId") Integer authorId);

    @Query("SELECT p FROM Product p JOIN p.bookAuthors ba WHERE ba.author.authorId = :authorId AND p.isActive = true ORDER BY p.createdAt DESC")
    List<Product> findLatestBooksByAuthorId(@Param("authorId") Integer authorId);

    Optional<Product> findByProductIdAndIsActiveTrue(Integer productId);

    @Query("SELECT p FROM Product p JOIN p.productCategory pc WHERE pc.category.categoryId = :categoryId AND p.isActive = true")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("""
        SELECT p FROM Product p 
        LEFT JOIN p.reviews r
        WHERE (:categoryId IS NULL OR EXISTS (
                SELECT pc FROM p.productCategory pc WHERE pc.category.categoryId = :categoryId
              ))
        AND (:minPrice IS NULL OR p.salePrice >= :minPrice)
        AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice)
        GROUP BY p
        HAVING (:minRating IS NULL OR AVG(r.rating) >= :minRating)
        """)
    List<Product> findWithFiltersAndRating(
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRating") Double minRating
    );
}
