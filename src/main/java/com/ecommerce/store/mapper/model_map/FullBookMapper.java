package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.request.model_request.FullBookUpdateRequest;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.dto.response.model_response.FullBookResponse;
import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.entity.Author;
import com.ecommerce.store.entity.Category;
import com.ecommerce.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FullBookMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "cartItem", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "productAssets", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    @Mapping(target = "productAttributeValues", ignore = true)
    Product toProduct(FullBookRequest request);

    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "type", constant = "IMAGE")
    @Mapping(target = "productAssets", ignore = true)
    @Mapping(target = "author", ignore = true)
    Asset toAsset(FullBookRequest request);

    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    Author toAuthor(FullBookRequest request);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "cartItem", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "productAssets", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    @Mapping(target = "productAttributeValues", ignore = true)
    Product toProduct(FullBookUpdateRequest request);

    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "type", constant = "IMAGE")
    @Mapping(target = "productAssets", ignore = true)
    @Mapping(target = "author", ignore = true)
    Asset toAsset(FullBookUpdateRequest request);

    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    Author toAuthor(FullBookUpdateRequest request);

    default CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(
                category.getCategoryId(),
                category.getParentId(),
                category.getCategoryName(),
                category.getSlug(),
                category.getIsActive(),
                category.getDescription()
        );
    }

    default List<CategoryResponse> toCategoryResponses(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    FullBookResponse toFullResponse(Product product, List<Category> categories, Asset asset, Author author);

    default FullBookResponse toFullResponse(Product product, Asset asset, Author author) {
        return toFullResponse(product, null, asset, author);
    }
}
