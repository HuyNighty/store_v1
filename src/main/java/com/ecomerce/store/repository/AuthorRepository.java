package com.ecomerce.store.repository;

import com.ecomerce.store.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    boolean existsByAuthorName(String authorName);
    Optional<List<Author>> findByAuthorNameContainingIgnoreCase(String authorName);
}
