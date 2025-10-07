package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "categories")
public class Category extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer categoryId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    Integer parentId = 0;

    @Column(nullable = false)
    @NotNull
    String categoryName;

    @Column(nullable = false)
    @NotNull
    String slug;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductCategory> productCategory;
}
