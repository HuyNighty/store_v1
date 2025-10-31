package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.ProductAsset;
import com.ecommerce.store.entity.BookAuthor;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductAssetMapper.class, BookAuthorMapper.class}
)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "cartItem", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "productAttributeValues", ignore = true)
    Product toProduct(ProductRequest request);

    @AfterMapping
    default void setRelations(@MappingTarget Product product) {
        if (product.getProductAssets() != null) {
            product.getProductAssets().forEach(asset -> asset.setProduct(product));
        }

        if (product.getBookAuthors() != null) {
            product.getBookAuthors().forEach(pa -> pa.setProduct(product));
        }
    }

    // Sử dụng Builder cho ProductResponse
    default ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .slug(product.getSlug())
                .productName(product.getProductName())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stockQuantity(product.getStockQuantity())
                .weightG(product.getWeightG())
                .isActive(product.getIsActive())
                .featured(product.getFeatured())
                .productAssets(mapProductAssets(product.getProductAssets()))
                .bookAuthors(mapBookAuthors(product.getBookAuthors()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    default List<ProductAssetResponse> mapProductAssets(Set<ProductAsset> productAssets) {
        if (productAssets == null) {
            return List.of();
        }

        return productAssets.stream()
                .map(this::toProductAssetResponse)
                .collect(Collectors.toList());
    }

    default ProductAssetResponse toProductAssetResponse(ProductAsset productAsset) {
        if (productAsset == null) {
            return null;
        }

        return ProductAssetResponse.builder()
                .assetId(productAsset.getAsset().getAssetId())
                .url(productAsset.getAsset().getUrl())
                .fileName(productAsset.getAsset().getFileName())
                .mimeType(productAsset.getAsset().getMimeType())
                .build();
    }

    default List<BookAuthorResponse> mapBookAuthors(Set<BookAuthor> bookAuthors) {
        if (bookAuthors == null) {
            return List.of();
        }

        return bookAuthors.stream()
                .map(this::toBookAuthorResponse)
                .collect(Collectors.toList());
    }

    default BookAuthorResponse toBookAuthorResponse(BookAuthor bookAuthor) {
        if (bookAuthor == null) {
            return null;
        }

        return BookAuthorResponse.builder()
                .authorId(bookAuthor.getAuthor().getAuthorId())
                .authorName(bookAuthor.getAuthor().getAuthorName())
                .authorRole(bookAuthor.getAuthorRole() != null ? bookAuthor.getAuthorRole() : null)
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}