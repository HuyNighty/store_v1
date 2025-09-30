package com.ecomerce.store.service.impl;

import com.ecomerce.store.dto.request.RegisterRequest;
import com.ecomerce.store.dto.response.RegisterResponse;
import com.ecomerce.store.entity.Customer;
import com.ecomerce.store.entity.User;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.AuthMapper;
import com.ecomerce.store.repository.UserRepository;
import com.ecomerce.store.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;

    AuthMapper authMapper;

    @Override
    @Transactional
    public RegisterResponse toResponse(RegisterRequest request) {
        if (userRepository.existsByUserName(request.userName())) {
            throw new AppException(ErrorCode.USER_NAME_EXISTED);
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = authMapper.toUser(request);
        Customer customer = authMapper.toCustomer(request);

        customer.setUser(user);
        user.setCustomer(customer);
        user.setEnabled(true);

        user = userRepository.save(user);

        return authMapper.toRegisterResponse(user, customer);
    }
}
