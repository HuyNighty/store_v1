package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.CategoryRequest;
import com.ecommerce.store.dto.request.model_request.CategoryUpdateRequest;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromRequest(CategoryUpdateRequest request, @MappingTarget Category entity);
}
