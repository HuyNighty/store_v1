package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.OrderResponse;
import com.ecommerce.store.entity.Customer;
import com.ecommerce.store.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.customer.customerId", target = "customerId")
    @Mapping(expression = "java(fullName(order.getUser().getCustomer()))", target = "fullName")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponse  toOrderResponse(Order order);

    default String fullName(Customer customer) {
        if (customer == null) return "N/A";
        if (customer.getLastName() == null) {
            return customer.getFirstName();
        }
        if (customer.getFirstName() == null) {
            return customer.getLastName();
        }
        return customer.getFirstName() + " " + customer.getLastName();
    }
}
