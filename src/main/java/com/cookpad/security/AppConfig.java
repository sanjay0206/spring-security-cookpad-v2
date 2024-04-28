package com.cookpad.security;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfig {

    @Value("${application.jwt.secretKey}")
    private String secretKey;

    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${application.jwt.tokenExpiration}")
    private Integer tokenExpiration;

    @Value("${application.cookie.cookieName}")
    private String cookieName;

    @Value("${application.cookie.cookieMaxAge}")
    private Integer cookieMaxAge;

    @Value("${whitelisted.endpoints}")
    private String whitelistedEndpoints;

    @Value("${blacklisted.endpoints}")
    private String blacklistedEndpoints;

    @Value("${application.login.endpoint}")
    private String loginEndpoint;

    @Value("${application.register.endpoint}")
    private String registerEndpoint;

    public byte[] getSecretKeyForSigning() {
        return secretKey.getBytes();
    }

    public String[] getWhitelistedEndpoints() {
        return Arrays.stream(whitelistedEndpoints.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public String[] getBlacklistedEndpoints() {
        return Arrays.stream(blacklistedEndpoints.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
