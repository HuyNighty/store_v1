package com.ecommerce.store.repository;

import com.ecommerce.store.entity.BookAuthor;
import com.ecommerce.store.entity.key.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
    List<BookAuthor> findByAuthorAuthorNameContainingIgnoreCase(String keyword);
    List<BookAuthor> findByProductProductId(Integer productId);
    List<BookAuthor> findByProduct_ProductId(Integer productId);

}
