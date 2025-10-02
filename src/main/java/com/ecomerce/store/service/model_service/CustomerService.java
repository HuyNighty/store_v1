package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.CustomerRequest;
import com.ecomerce.store.dto.response.model_response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse updateCustomer(String customerId, CustomerRequest request);
    CustomerResponse getCustomerById(String customerId);
    List<CustomerResponse> getAllCustomers();
}
