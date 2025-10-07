package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);
    boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String categoryName, Integer id);
    List<Category> findAllByDeletedAtIsNull();

}
