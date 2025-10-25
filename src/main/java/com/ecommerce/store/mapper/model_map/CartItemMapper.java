package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.CartItemRequest;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.dto.response.model_response.CartItemResponse;
import com.ecommerce.store.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem toEntity(CartItemRequest request);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.sku", target = "sku")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.salePrice", target = "salePrice")
    @Mapping(source = "product.stockQuantity", target = "stockQuantity")
    @Mapping(source = "product.featured", target = "featured")
    @Mapping(expression = "java(getUrl(cartItem))", target = "url")
    @Mapping(expression = "java(getAuthorName(cartItem))", target = "authorName")
    @Mapping(expression = "java(getBookAuthors(cartItem))", target = "bookAuthors")
    @Mapping(expression = "java(totalPrice(cartItem))", target = "totalPrice")
    CartItemResponse toResponse(CartItem cartItem);

    List<CartItemResponse> toResponseList(List<CartItem> entities);

    default BigDecimal totalPrice(CartItem cartItem) {
        if (cartItem.getUnitPrice() == null || cartItem.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    default String getUrl(CartItem cartItem) {
        if (cartItem.getProduct() == null || cartItem.getProduct().getProductAssets() == null) {
            return "/images/default-book.jpg";
        }

        return cartItem.getProduct().getProductAssets().stream()
                .findFirst()
                .map(productAsset -> productAsset.getAsset().getUrl())
                .orElse("/images/default-book.jpg");
    }

    default String getAuthorName(CartItem cartItem) {
        if (cartItem.getProduct() == null || cartItem.getProduct().getBookAuthors() == null) {
            return "Đang cập nhật";
        }

        return cartItem.getProduct().getBookAuthors().stream()
                .findFirst()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .orElse("Đang cập nhật");
    }

    default List<BookAuthorResponse> getBookAuthors(CartItem cartItem) {
        if (cartItem.getProduct() == null || cartItem.getProduct().getBookAuthors() == null) {
            return List.of();
        }

        return cartItem.getProduct().getBookAuthors().stream()
                .map(bookAuthor -> new BookAuthorResponse(
                        cartItem.getProduct().getProductId(),
                        bookAuthor.getAuthor().getAuthorId(),
                        cartItem.getProduct().getProductName(),
                        bookAuthor.getAuthor().getAuthorName(),
                        bookAuthor.getAuthorRole()
                ))
                .toList();
    }
}