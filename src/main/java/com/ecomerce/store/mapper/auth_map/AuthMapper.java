package com.ecomerce.store.mapper.auth_map;

import com.ecomerce.store.dto.request.auth_request.RegisterRequest;
import com.ecomerce.store.dto.response.auth_response.LoginResponse;
import com.ecomerce.store.dto.response.auth_response.RegisterResponse;
import com.ecomerce.store.dto.response.auth_response.UserInfoResponse;
import com.ecomerce.store.entity.Customer;
import com.ecomerce.store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toUser(RegisterRequest registerRequest);

    Customer toCustomer(RegisterRequest registerRequest);

    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(expression = "java(customer.getFirstName()+ \" \" + customer.getLastName())", target = "fullName")
    @Mapping(source = "customer.phoneNumber", target = "phoneNumber")
    @Mapping(source = "customer.address", target = "address")
    RegisterResponse toRegisterResponse(User user, Customer customer);

    @Mapping(expression = "java(getFullName(customer))", target = "fullName")
    @Mapping(expression = "java(getLoyaltyPoints(customer))", target = "loyaltyPoints")
    @Mapping(expression = "java(getRoles(user))", target = "roles")
    LoginResponse toLoginResponse(User user, Customer customer);

    @Mapping(expression = "java(getFullName(customer))", target = "fullName")
    @Mapping(expression = "java(getLoyaltyPoints(customer))", target = "loyaltyPoints")
    @Mapping(expression = "java(getRoles(user))", target = "roles")
    UserInfoResponse toUserInfoResponse(User user, Customer customer);

    default Set<String> getRoles(User user) {
        if (user.getUserRoles() == null) return Set.of();
        return user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toSet());
    }

    default String getFullName(Customer customer) {
        if (customer == null) {
            return "System Admin";
        }
        return customer.getFirstName() + " " + customer.getLastName();
    }

    default Integer getLoyaltyPoints(Customer customer) {
        if (customer == null) {
            return 0;
        }
        return customer.getLoyaltyPoints();
    }
}
