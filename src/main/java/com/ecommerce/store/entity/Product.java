package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productId;

    @Column(nullable = false, unique = true)
    String sku;

    @Column(unique = true)
    String slug;

    @Column(nullable = false)
    String productName;

    @Column(nullable = false)
    BigDecimal price;

    BigDecimal salePrice;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    Integer stockQuantity = 0;

    BigDecimal weightG;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean isActive = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean featured = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> cartItem;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderItem> orderItem;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductAsset>  productAssets;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductCategory> productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BookAuthor> bookAuthors;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductAttributeValue> productAttributeValues;
}
