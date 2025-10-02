package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.response.model_response.CustomerResponse;
import com.ecomerce.store.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);
}
