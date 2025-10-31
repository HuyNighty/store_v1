package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.entity.Customer;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.CustomerMapper;
import com.ecommerce.store.repository.CustomerRepository;
import com.ecommerce.store.service.model_service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.firstName() != null && !request.firstName().isBlank()) {
            customer.setFirstName(request.firstName());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            customer.setLastName(request.lastName());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            boolean exists = customerRepository.existsByPhoneNumber(request.phoneNumber());
            if (exists && !customer.getPhoneNumber().equals(request.phoneNumber())) {
                throw new AppException(ErrorCode.PHONE_ALREADY_EXISTED);
            }
            customer.setPhoneNumber(request.phoneNumber());
        }

        if (request.address() != null && !request.address().isBlank()) {
            customer.setAddress(request.address());
        }

        if (request.loyaltyPoints() != null) {
            customer.setLoyaltyPoints(request.loyaltyPoints());
        }

        customer.setUpdatedAt(LocalDateTime.now());

        Customer updated = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(updated);
    }

    @Override
    public CustomerResponse getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }

    @Override
    public CustomerResponse getCustomerByUserId(String userId) {
        Customer customer = customerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        return customerMapper.toCustomerResponse(customer);
    }
}
