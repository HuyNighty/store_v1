package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.request.model_request.CartItemRequest;
import com.ecomerce.store.dto.response.model_response.CartItemResponse;
import com.ecomerce.store.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem toEntity(CartItemRequest request);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(expression = "java(totalPrice(cartItem))", target = "totalPrice")
    CartItemResponse toResponse(CartItem cartItem);

    List<CartItemResponse> toResponseList(List<CartItem> entities);

    default BigDecimal totalPrice(CartItem cartItem) {
        if (cartItem.getUnitPrice() == null || cartItem.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return cartItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

}
