package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.OrderItemResponse;
import com.ecommerce.store.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.productAssets", target = "productUrl", qualifiedByName = "getFirstImageUrl")
    @Mapping(source = "product.productCategory", target = "categoryName", qualifiedByName = "getCategoryName")
    @Mapping(source = "product.bookAuthors", target = "authorName", qualifiedByName = "getAuthorName")
    @Mapping(expression = "java(calculateSubTotal(orderItem))", target = "subTotal")
    OrderItemResponse toResponse(OrderItem orderItem);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);

    default BigDecimal calculateSubTotal(OrderItem orderItem) {
        if (orderItem.getUnitPrice() == null || orderItem.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }

    @Named("getFirstImageUrl")
    default String getFirstImageUrl(Set<ProductAsset> productAssets) {
        if (productAssets == null || productAssets.isEmpty()) {
            return "/images/default-book.jpg";
        }

        Optional<ProductAsset> firstAsset = productAssets.stream()
                .filter(pa -> pa.getAsset() != null && pa.getAsset().getUrl() != null)
                .min(Comparator.comparing(ProductAsset::getOrdinal));

        return firstAsset
                .map(ProductAsset::getAsset)
                .map(asset -> {
                    String url = asset.getUrl();
                    if (url != null && !url.startsWith("http") && !url.startsWith("/")) {
                        return "/images/" + url;
                    }
                    return url;
                })
                .orElse("/images/default-book.jpg");
    }

    @Named("getCategoryName")
    default String getCategoryName(Set<ProductCategory> productCategories) {
        if (productCategories == null || productCategories.isEmpty()) {
            return "Không có danh mục";
        }

        Optional<ProductCategory> firstCategory = productCategories.stream()
                .findFirst();

        return firstCategory
                .map(ProductCategory::getCategory)
                .map(Category::getCategoryName)
                .orElse("Không có danh mục");
    }

    @Named("getAuthorName")
    default String getAuthorName(Set<BookAuthor> bookAuthors) {
        if (bookAuthors == null || bookAuthors.isEmpty()) {
            return "Không có tác giả";
        }

        Optional<BookAuthor> firstAuthor = bookAuthors.stream()
                .findFirst();

        return firstAuthor
                .map(BookAuthor::getAuthor)
                .map(Author::getAuthorName)
                .orElse("Không có tác giả");
    }
}