package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
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
                .categories(mapCategories(product.getProductCategory()))
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

    default List<CategoryResponse> mapCategories(Set<ProductCategory> productCategories) {
        if (productCategories == null) {
            return List.of();
        }

        return productCategories.stream()
                .map(ProductCategory::getCategory)
                .filter(Objects::nonNull)
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    default CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .parentId(category.getParentId())
                .isActive(category.getIsActive())
                .build();
    }

    default Double calculateAverageRating(Product product) {
        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            return 0.0;
        }

        return product.getReviews().stream()
                .mapToDouble(review -> review.getRating() != null ? review.getRating().doubleValue() : 0.0)
                .average()
                .orElse(0.0);
    }

    default Integer calculateReviewCount(Product product) {
        return product.getReviews() != null ? product.getReviews().size() : 0;
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}