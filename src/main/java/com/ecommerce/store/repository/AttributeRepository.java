package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    boolean existsByAttributeNameIgnoreCase(String attributeName);
    List<Attribute> findByAttributeNameContainingIgnoreCase(String attributeName);
}
