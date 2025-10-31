package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse updateCustomer(String customerId, CustomerRequest request);
    CustomerResponse getCustomerById(String customerId);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerByUserId(String userId);
}
