package com.ecomerce.store.entity;

import com.ecomerce.store.entity.key.ProductCategoryId;
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
@Table(name = "product_categories")
public class ProductCategory {

    @EmbeddedId
    ProductCategoryId productCategoryId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
}
