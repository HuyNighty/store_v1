package com.ecommerce.store.repository;

import com.ecommerce.store.entity.ProductAsset;
import com.ecommerce.store.entity.key.ProductAssetId;
import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, ProductAssetId> {

    List<ProductAsset> findByProductProductId(Integer productId);
    List<ProductAsset> findByType(ProductAssetType type);
    List<ProductAsset> findByOrdinal(Integer ordinal);
}
