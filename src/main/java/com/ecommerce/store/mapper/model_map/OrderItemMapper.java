package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.OrderItemResponse;
import com.ecommerce.store.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(expression = "java(subTotal(orderItem))", target = "subTotal")
    OrderItemResponse toResponse(OrderItem orderItem);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);

    default BigDecimal subTotal(OrderItem orderItem) {

        if (orderItem.getProduct().getPrice() == null || orderItem.getQuantity() == null) {
            return BigDecimal.ZERO;
        }

        return orderItem.getProduct().getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
    }
}
