package com.ecommerce.store.service.impl.auth_impl;

import com.ecommerce.store.dto.request.auth_request.IntrospectRequest;
import com.ecommerce.store.dto.request.auth_request.RefreshTokenRequest;
import com.ecommerce.store.dto.response.auth_response.IntrospectResponse;
import com.ecommerce.store.dto.response.auth_response.RefreshTokenResponse;
import com.ecommerce.store.entity.InvalidatedToken;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.repository.InvalidatedTokenRepository;
import com.ecommerce.store.repository.RefreshTokenRepository;
import com.ecommerce.store.repository.UserRepository;
import com.ecommerce.store.service.auth_service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.validDuration}")
    Long VALIDATION_DURATION;

    @NonFinal
    @Value("${jwt.refreshableDuration}")
    Long REFRESHABLE_DURATION;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("Store_Book")
                .issueTime(new Date())
                .claim("scope", buildScope(user))
                .claim("id", user.getUserId())
                .expirationTime(Date.from(Instant.now().plus(VALIDATION_DURATION, ChronoUnit.SECONDS)))
                .jwtID(UUID.randomUUID().toString())
                .build();
        return signAndSerialize(header, jwtClaimsSet);
    }

    @Override
    public String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("Store_Book")
                .issueTime(new Date())
                .claim("id", user.getUserId())
                .expirationTime(Date.from(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)))
                .jwtID(UUID.randomUUID().toString())
                .build();
        return signAndSerialize(header, jwtClaimsSet);
    }

    private String buildScope(User user) {
        Set<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> permissions = user.getUserRoles()
                .stream()
                .flatMap(userRole -> userRole.getRole().getRolePermissions().stream())
                .map(rolePermission -> rolePermission.getPermission().getPermissionName())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        StringJoiner stringJoiner = new StringJoiner(" ");
        roles.forEach(stringJoiner::add);
        permissions.forEach(stringJoiner::add);

        return stringJoiner.toString();
    }

    @Override
    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID())
        ) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request)
            throws ParseException, JOSEException {

        SignedJWT signedJWT = verifyToken(request.refreshToken());

        String oldJti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder().jti(oldJti).expiredAt(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String newAccess = generateAccessToken(user);
        String newRefresh = generateRefreshToken(user);

        refreshTokenRepository.findByToken(request.refreshToken()).ifPresent(old -> {
            old.setToken(newRefresh);
            old.setExpiredAt(LocalDateTime.now().plusSeconds(REFRESHABLE_DURATION));
            refreshTokenRepository.save(old);
        });

        return RefreshTokenResponse.builder()
                .success(true)
                .refreshToken(newRefresh)
                .accessToken(newAccess)
                .build();
    }



    @Override
    public Long getRefreshDurationSeconds() {
        return REFRESHABLE_DURATION;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {

        String token = request.token();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse
                .builder()
                .verified(isValid)
                .build();
    }

    private String signAndSerialize(JWSHeader header, JWTClaimsSet jwtClaimsSet) {
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Failed to sign JWT", e);
            throw new RuntimeException("Unable to sign key");
        }
    }
}
