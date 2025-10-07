package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import com.ecommerce.store.enums.entity_enums.AttributeEnums.DataType;
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
@Table(name = "attributes")
public class Attribute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer attributeId;

    @Column(nullable = false, unique = true, length = 50)
    String attributeName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  length = 20)
    DataType dataType;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductAttributeValue> productAttributeValues;
}
