package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.AttributeRequest;
import com.ecommerce.store.dto.response.model_response.AttributeResponse;
import com.ecommerce.store.entity.Attribute;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    Attribute toEntity(AttributeRequest request);

    AttributeResponse toResponse(Attribute entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAttributeFromRequest(AttributeRequest request, @MappingTarget Attribute attribute);
}
