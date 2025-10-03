package com.ecomerce.store.service.impl.auth_impl;

import com.ecomerce.store.dto.request.auth_request.LoginRequest;
import com.ecomerce.store.dto.request.auth_request.LogoutRequest;
import com.ecomerce.store.dto.request.auth_request.RegisterRequest;
import com.ecomerce.store.dto.response.auth_response.LoginResponse;
import com.ecomerce.store.dto.response.auth_response.RegisterResponse;
import com.ecomerce.store.dto.response.auth_response.UserInfoResponse;
import com.ecomerce.store.entity.*;
import com.ecomerce.store.entity.key.UserRoleId;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.auth_map.AuthMapper;
import com.ecomerce.store.repository.*;
import com.ecomerce.store.service.auth_service.AuthService;
import com.ecomerce.store.service.auth_service.JwtService;
import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthServiceImpl implements AuthService {
    JwtService jwtService;
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    CartRepository cartRepository;

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
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        LoginResponse response = authMapper.toLoginResponse(user, user.getCustomer());
        response.setToken(token);

        return response;
    }

    @Override
    public UserInfoResponse userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USER_NAME_NOT_FOUND));

        return authMapper.toUserInfoResponse(user, user.getCustomer());
    }

    @Override
    public Set<UserInfoResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    Customer customer = user.getCustomer();
                    return authMapper.toUserInfoResponse(user, customer);
                })
                .collect(Collectors.toSet());
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = jwtService.verifyToken(request.token());

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .jti(jti)
                .expiredAt(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
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

        Role defaultRole = roleRepository.findByRoleNameIgnoreCase("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        UserRoleId id = new UserRoleId(user.getUserId(), defaultRole.getRoleId());

        UserRole userRole = UserRole.builder()
                .userRoleId(id)
                .user(user)
                .role(defaultRole)
                .build();

        userRoleRepository.save(userRole);

        Cart cart = Cart.builder()
                .user(user)
                .cartItems(new HashSet<>())
                .build();

        cartRepository.save(cart);

        return authMapper.toRegisterResponse(user, customer);
    }

}
