package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);
}
