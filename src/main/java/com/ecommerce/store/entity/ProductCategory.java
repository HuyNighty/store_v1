package com.ecommerce.store.entity;

import com.ecommerce.store.entity.key.ProductCategoryId;
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

    // Static factory method để tạo ProductCategory dễ dàng
    public static ProductCategory create(Product product, Category category) {
        ProductCategoryId id = new ProductCategoryId(product.getProductId(), category.getCategoryId());
        return ProductCategory.builder()
                .productCategoryId(id)
                .product(product)
                .category(category)
                .build();
    }
}
