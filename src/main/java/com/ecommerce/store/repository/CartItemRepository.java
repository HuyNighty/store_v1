package com.ecommerce.store.repository;

import com.ecommerce.store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT ci FROM CartItem ci " +
            "LEFT JOIN FETCH ci.product p " +
            "LEFT JOIN FETCH p.productAssets pa " +
            "LEFT JOIN FETCH pa.asset " +
            "LEFT JOIN FETCH p.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE ci.cart.cartId = :cartId")
    List<CartItem> findByCartCartId(@Param("cartId") Integer cartId);

    @Query("SELECT ci FROM CartItem ci " +
            "LEFT JOIN FETCH ci.product p " +
            "LEFT JOIN FETCH p.productAssets pa " +
            "LEFT JOIN FETCH pa.asset " +
            "LEFT JOIN FETCH p.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE ci.cart.cartId = :cartId AND p.productId = :productId")
    Optional<CartItem> findByCartCartIdAndProductProductId(@Param("cartId") Integer cartId,
                                                           @Param("productId") Integer productId);

    void deleteByCartCartId(Integer cartId);

    List<CartItem> findByCartUserUserId(String userId);

    int countByCartCartId(Integer cartId);
}