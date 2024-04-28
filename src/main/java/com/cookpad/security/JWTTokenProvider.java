package com.cookpad.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookpad.entities.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTTokenProvider {

    private final AppConfig appConfig;

    @Autowired
    public JWTTokenProvider(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    public String generateToken(Authentication authResult) {
        log.info("Authentication: " + authResult);

//        String role = authResult.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .findFirst()
//                .orElse("");

        List<String> roles = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String subject = authResult.getName();
        Date issuedAt = new Date();
        Date expiresAt = Date.from(issuedAt.toInstant().plus(Duration.ofMinutes(appConfig.getTokenExpiration())));

        byte[] secretKey = appConfig.getSecretKeyForSigning();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withSubject(subject)
                .withClaim("roles", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
        log.info("JWT token is: {}", token);
        return token;
    }

    public boolean isValidToken(String token) {
        byte[] secretKey = appConfig.getSecretKeyForSigning();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        log.info("Token is verified");
        return true;
    }
}
