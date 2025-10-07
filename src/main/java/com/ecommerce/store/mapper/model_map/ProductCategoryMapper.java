package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductCategoryRequest;
import com.ecommerce.store.dto.response.model_response.ProductCategoryResponse;
import com.ecommerce.store.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    ProductCategoryResponse toResponse(ProductCategory entity);
}
