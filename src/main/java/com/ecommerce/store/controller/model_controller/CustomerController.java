package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.service.model_service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerController {

    CustomerService customerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        return ApiResponse
                .<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> getCustomerById(@PathVariable String customerId) {
        return ApiResponse
                .<CustomerResponse>builder()
                .result(customerService.getCustomerById(customerId))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{customerId}")
    public ApiResponse<CustomerResponse> updateCustomer(
            @PathVariable String customerId,
            @RequestBody CustomerRequest request) {
        return ApiResponse
                .<CustomerResponse>builder()
                .result(customerService.updateCustomer(customerId, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ApiResponse<CustomerResponse> getCustomerByUserId(@PathVariable String userId) {
        return ApiResponse
                .<CustomerResponse>builder()
                .result(customerService.getCustomerByUserId(userId))
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CustomerResponse> getMyProfile() {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCurrentCustomer())
                .build();
    }

    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<String>builder()
                .result(customerService.uploadProfileImage(file))
                .message("Profile image uploaded successfully")
                .build();
    }

    @DeleteMapping("/profile-image")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> removeProfileImage() {
        customerService.removeProfileImage();
        return ApiResponse.<Void>builder()
                .message("Profile image removed successfully")
                .build();
    }
}
