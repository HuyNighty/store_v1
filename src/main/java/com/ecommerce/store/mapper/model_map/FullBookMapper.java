package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.response.model_response.FullBookResponse;
import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.entity.Author;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;
import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    // Asset mappings
    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "type", constant = "IMAGE")
    @Mapping(target = "productAssets", ignore = true)
    @Mapping(target = "author", ignore = true)
    Asset toAsset(FullBookRequest request);

    // Author mappings
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    Author toAuthor(FullBookRequest request);

    @Mapping(source = "author.authorBio", target = "authorBio")
    @Mapping(source = "author.authorBornDate", target = "authorBornDate")
    @Mapping(source = "author.authorDeathDate", target = "authorDeathDate")
    @Mapping(source = "author.authorBio", target = "authorBio")
    default FullBookResponse toFullResponse(Product product, Asset asset, Author author) {
        return new FullBookResponse(
                product.getSku(),
                product.getSlug(),
                product.getProductName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getStockQuantity(),
                product.getWeightG(),
                product.getIsActive(),
                product.getFeatured(),
                asset != null ? asset.getUrl() : null,
                asset != null ? asset.getFileName() : null,
                asset != null ? asset.getMimeType() : null,
                asset != null ? asset.getWidth() : null,
                asset != null ? asset.getHeight() : null,
                asset != null ? asset.getSizeBytes() : null,
                author != null ? author.getAuthorName() : null,
                author != null ? author.getBio() : null,
                author != null ? author.getBornDate() : null,
                author != null ? author.getDeathDate() : null,
                author != null ? author.getNationality() : null
        );
    }
}