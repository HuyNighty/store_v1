package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductAttributeValueRequest;
import com.ecommerce.store.dto.response.model_response.ProductAttributeValueResponse;
import com.ecommerce.store.entity.ProductAttributeValue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {

    ProductAttributeValue toEntity(ProductAttributeValueRequest request);

    @Mapping(target = "productId", source = "productAttributeValueId.productId")
    @Mapping(target = "attributeId", source = "productAttributeValueId.attributeId")
    @Mapping(target = "attributeName", source = "attribute.attributeName")
    ProductAttributeValueResponse toResponse(ProductAttributeValue entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductAttributeValueRequest request, @MappingTarget ProductAttributeValue entity);
}
