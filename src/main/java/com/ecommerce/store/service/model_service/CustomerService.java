package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    CustomerResponse updateCustomer(String customerId, CustomerRequest request);
    CustomerResponse getCustomerById(String customerId);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerByUserId(String userId);
    CustomerResponse getCurrentCustomer();
    String uploadProfileImage(MultipartFile file);
    void removeProfileImage();
}
