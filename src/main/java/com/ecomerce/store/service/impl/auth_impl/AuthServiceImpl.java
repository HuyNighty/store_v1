package com.ecomerce.store.service.impl.auth_impl;

import com.ecomerce.store.dto.request.auth_request.LoginRequest;
import com.ecomerce.store.dto.request.auth_request.RegisterRequest;
import com.ecomerce.store.dto.response.auth_response.LoginResponse;
import com.ecomerce.store.dto.response.auth_response.RegisterResponse;
import com.ecomerce.store.entity.Customer;
import com.ecomerce.store.entity.User;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.auth_map.AuthMapper;
import com.ecomerce.store.repository.CustomerRepository;
import com.ecomerce.store.repository.UserRepository;
import com.ecomerce.store.service.auth_service.AuthService;
import com.ecomerce.store.service.auth_service.JwtService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthServiceImpl implements AuthService {
    JwtService jwtService;
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    AuthMapper authMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user;
        if (request.identifier().contains("@")) {
            user = userRepository.findByEmail(request.identifier())
                    .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        } else {
            user = userRepository.findByUserName(request.identifier())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NAME_NOT_FOUND));
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.setLastLoginAt(LocalDateTime.now());

        String token = jwtService.generateToken(user);

        userRepository.save(user);

        Customer customer = user.getCustomer();

        LoginResponse response = authMapper.toLoginResponse(user, customer);
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
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
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);

        user = userRepository.save(user);

        return authMapper.toRegisterResponse(user, customer);
    }
}
