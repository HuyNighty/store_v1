package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.response.model_response.OrderItemResponse;
import com.ecomerce.store.dto.response.model_response.OrderResponse;
import com.ecomerce.store.entity.Customer;
import com.ecomerce.store.entity.Order;
import com.ecomerce.store.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(expression = "java(fullName(order.getUser().getCustomer()))", target = "fullName")
    OrderResponse  toOrderResponse(Order order);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(expression = "java(subTotal(item))", target = "subTotal")
    OrderItemResponse toOrderItemResponse(OrderItem item);


    default BigDecimal subTotal(OrderItem item) {

        if (item.getProduct().getPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }

        return item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
    }

    default String fullName(Customer customer) {
        if (customer.getLastName() == null) {
            return customer.getFirstName();
        }
        if (customer.getFirstName() == null) {
            return customer.getLastName();
        }
        return customer.getFirstName() + " " + customer.getLastName();
    }
}
