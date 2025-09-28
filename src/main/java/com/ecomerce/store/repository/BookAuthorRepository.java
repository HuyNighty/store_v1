package com.ecomerce.store.repository;

import com.ecomerce.store.entity.BookAuthor;
import com.ecomerce.store.entity.key.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
}
