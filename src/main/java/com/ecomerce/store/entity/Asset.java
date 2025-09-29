package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseSoftDeleteEntity;
import com.ecomerce.store.enums.entity_enums.AssetEnums.AssetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(nullable = false, unique = true)
    String url;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    AssetType type;

    @Pattern(regexp = "^[a-zA-Z0-9]+/[a-zA-Z0-9\\-\\.]+$")
    String mimeType;

    Integer width;

    Integer height;

    Long sizeBytes;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductAsset> productAssets;

    @OneToOne(mappedBy = "asset",  cascade = CascadeType.ALL, orphanRemoval = true)
    Author author;
}
