package com.ecomerce.store.repository;

import com.ecomerce.store.entity.ProductAsset;
import com.ecomerce.store.entity.key.ProductAssetId;
import com.ecomerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, ProductAssetId> {

    List<ProductAsset> findByProductProductId(Integer productId);
    List<ProductAsset> findByType(ProductAssetType type);
    List<ProductAsset> findByOrdinal(Integer ordinal);
}
