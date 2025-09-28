package com.ecomerce.store.repository;

import com.ecomerce.store.entity.ProductAsset;
import com.ecomerce.store.entity.key.ProductAssetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, ProductAssetId> {
}
