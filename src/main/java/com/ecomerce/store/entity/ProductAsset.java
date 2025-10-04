package com.ecomerce.store.entity;

import com.ecomerce.store.entity.key.ProductAssetId;
import com.ecomerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_assets")
public class ProductAsset {

    @EmbeddedId
    ProductAssetId productAssetId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @MapsId("assetId")
    @JoinColumn(name = "asset_id", nullable = false)
    Asset asset;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    ProductAssetType type;

    @Builder.Default
    @Column(nullable = false)
    Integer ordinal = 0;

    @PrePersist
    void prePersist() {
        if (ordinal == null) ordinal = 0;
    }
}
