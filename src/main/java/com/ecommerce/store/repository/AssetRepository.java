package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {

    boolean existsByUrl(String url);
    Optional<Asset> findByType(AssetType assetType);
}
