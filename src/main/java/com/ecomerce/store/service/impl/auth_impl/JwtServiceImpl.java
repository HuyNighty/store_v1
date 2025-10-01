package com.ecomerce.store.service.impl.auth_impl;

import com.ecomerce.store.dto.request.auth_request.IntrospectRequest;
import com.ecomerce.store.dto.request.auth_request.RefreshTokenRequest;
import com.ecomerce.store.dto.response.auth_response.IntrospectResponse;
import com.ecomerce.store.dto.response.auth_response.RefreshTokenResponse;
import com.ecomerce.store.entity.InvalidatedToken;
import com.ecomerce.store.entity.User;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.repository.InvalidatedTokenRepository;
import com.ecomerce.store.repository.UserRepository;
import com.ecomerce.store.service.auth_service.JwtService;
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
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet
                .Builder()
                .subject(user.getUserName())
                .issuer("Store_Book")
                .issueTime(new Date())
                .claim("scope", buildScope(user))
                .expirationTime(Date.from(Instant.now().plus(VALIDATION_DURATION, ChronoUnit.SECONDS)))
                .jwtID(UUID.randomUUID().toString())
                .build();

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

        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .jti(jti)
                .expiredAt(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        String userName = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String refreshToken = generateToken(user);

        return RefreshTokenResponse
                .builder()
                .success(true)
                .refreshToken(refreshToken)
                .build();
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
}
