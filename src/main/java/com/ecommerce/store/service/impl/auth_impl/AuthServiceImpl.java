package com.ecommerce.store.service.impl.auth_impl;

import com.ecommerce.store.dto.request.auth_request.LoginRequest;
import com.ecommerce.store.dto.request.auth_request.LogoutRequest;
import com.ecommerce.store.dto.request.auth_request.RegisterRequest;
import com.ecommerce.store.dto.response.auth_response.*;
import com.ecommerce.store.entity.*;
import com.ecommerce.store.entity.*;
import com.ecommerce.store.entity.key.UserRoleId;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.auth_map.AuthMapper;
import com.ecommerce.store.repository.*;
import com.ecommerce.store.repository.*;
import com.ecommerce.store.service.auth_service.AuthService;
import com.ecommerce.store.service.auth_service.JwtService;
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
    RefreshTokenRepository refreshTokenRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    CartRepository cartRepository;
    CustomerRepository customerRepository;

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

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        LocalDateTime expiry = LocalDateTime.now().plusSeconds(jwtService.getRefreshDurationSeconds());

        RefreshToken rt = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(expiry)
                .build();

        refreshTokenRepository.deleteByUserId(user.getUserId());
        refreshTokenRepository.save(rt);

        LoginResponse response = authMapper.toLoginResponse(user, user.getCustomer());
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken);

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

    @Override
    public MyInfoResponse myInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String userName = authentication.getName();

        User user = userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED));

        Customer customer = customerRepository.findByUser_UserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.CUSTOMER_NOT_FOUND)); // Tạo ErrorCode mới

        MyInfoSource myInfoSource = new MyInfoSource(user, customer);
        return authMapper.toMyInfoResponse(myInfoSource);
    }
}
