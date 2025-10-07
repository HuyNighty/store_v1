package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseSoftDeleteEntity;
import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets")
public class Asset extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer assetId;

    @Column(nullable = false, unique = true)
    String url;

    String fileName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AssetType type;

    String mimeType;

    Integer width;

    Integer height;

    Long sizeBytes;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductAsset> productAssets;

    @OneToOne(mappedBy = "asset",  cascade = CascadeType.ALL, orphanRemoval = true)
    Author author;
}
