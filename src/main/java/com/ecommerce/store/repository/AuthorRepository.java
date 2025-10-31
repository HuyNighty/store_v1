package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    boolean existsByAuthorName(String authorName);
    Optional<List<Author>> findByAuthorNameContainingIgnoreCase(String authorName);
    @Query("SELECT a FROM Author a LEFT JOIN a.bookAuthors ba GROUP BY a ORDER BY COUNT(ba) DESC LIMIT 10")
    List<Author> findPopularAuthors();

    @Query("SELECT COUNT(ba) FROM BookAuthor ba WHERE ba.author.authorId = :authorId")
    Integer countBooksByAuthorId(@Param("authorId") Integer authorId);
}
