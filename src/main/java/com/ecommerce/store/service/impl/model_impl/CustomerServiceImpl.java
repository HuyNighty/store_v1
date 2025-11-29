package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.CustomerRequest;
import com.ecommerce.store.dto.response.model_response.CustomerResponse;
import com.ecommerce.store.entity.Customer;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.CustomerMapper;
import com.ecommerce.store.repository.CustomerRepository;
import com.ecommerce.store.repository.UserRepository;
import com.ecommerce.store.service.model_service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    UserRepository userRepository;
    CustomerMapper customerMapper;

    private final String UPLOAD_DIR = "uploads/profile-images/";

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

    public String uploadProfileImage(MultipartFile file) {
        // Validate file
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            Customer customer = customerRepository.findByUser(user)
                    .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName != null ?
                    originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
            String fileName = UUID.randomUUID().toString() + fileExtension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            if (customer.getProfileImage() != null) {
                deleteOldImage(customer.getProfileImage());
            }

            String imageUrl = "/uploads/profile-images/" + fileName;

            String absoluteUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/Store")
                    .path(imageUrl)
                    .toUriString();

            customer.setProfileImage(imageUrl);
            customer.setUpdatedAt(LocalDateTime.now());
            customerRepository.save(customer);

            return absoluteUrl;

        } catch (IOException e) {
            log.error("Error uploading profile image", e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public void removeProfileImage() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (customer.getProfileImage() != null) {
            deleteOldImage(customer.getProfileImage());

            customer.setProfileImage(null);
            customer.setUpdatedAt(LocalDateTime.now());
            customerRepository.save(customer);
        }
    }

    private void deleteOldImage(String imageUrl) {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Could not delete old image file: {}", imageUrl, e);
        }
    }

    public CustomerResponse getCurrentCustomer() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        return customerMapper.toCustomerResponse(customer);
    }
}