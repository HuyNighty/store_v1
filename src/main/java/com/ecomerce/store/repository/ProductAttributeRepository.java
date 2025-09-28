package com.ecomerce.store.repository;

import com.ecomerce.store.entity.ProductAttributeValue;
import com.ecomerce.store.entity.key.ProductAttributeValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeValue, ProductAttributeValueId> {
}
