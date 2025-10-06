package com.ecommerce.store.repository;

import com.ecommerce.store.entity.ProductAttributeValue;
import com.ecommerce.store.entity.key.ProductAttributeValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeValue, ProductAttributeValueId> {
}
