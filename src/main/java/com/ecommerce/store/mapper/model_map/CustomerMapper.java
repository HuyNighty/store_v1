package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.entity.Customer;
import com.ecommerce.store.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.orders", target = "totalOrders", qualifiedByName = "countOrders")
    @Mapping(source = "user.orders", target = "totalSpent", qualifiedByName = "calculateTotalSpent")
    @Mapping(source = "user.userId", target = "userId")
    CustomerResponse toCustomerResponse(Customer customer);

    @Named("countOrders")
    default Double countOrders(Set<Order> orders) {
        if (orders == null) return 0.0;
        return (double) orders.size();
    }

    @Named("calculateTotalSpent")
    default Double calculateTotalSpent(Set<Order> orders) {
        if (orders == null) return 0.0;

        return orders.stream()
                .filter(order -> order.getTotalAmount() != null)
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
    }
}