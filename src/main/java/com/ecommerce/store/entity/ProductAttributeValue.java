package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import com.ecommerce.store.entity.key.ProductAttributeValueId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_attribute_values")
public class ProductAttributeValue extends BaseTimeEntity {

    @EmbeddedId
    ProductAttributeValueId productAttributeValueId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id", nullable = false)
    Attribute attribute;

    String valueText;

    Double valueNumber;

    LocalDate valueDate;
}
