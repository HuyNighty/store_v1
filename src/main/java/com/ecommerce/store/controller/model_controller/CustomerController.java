package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.service.model_service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    CustomerService customerService;

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        return ApiResponse
                .<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> getCustomerById(@PathVariable String customerId) {
        return ApiResponse
                .<CustomerResponse>builder()
                .result(customerService.getCustomerById(customerId))
                .build();
    }

    @PatchMapping("/{customerId}")
    public ApiResponse<CustomerResponse> updateCustomer(
            @PathVariable String customerId,
            @RequestBody CustomerRequest request) {
        return ApiResponse
                .<CustomerResponse>builder()
                .result(customerService.updateCustomer(customerId, request))
                .build();
    }
}
