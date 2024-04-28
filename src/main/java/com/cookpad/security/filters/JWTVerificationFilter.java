package com.cookpad.security.filters;

import com.cookpad.exceptions.RecipeAPIException;
import com.cookpad.responses.ErrorDetailsResponse;
import com.cookpad.security.AppConfig;
import com.cookpad.security.CustomUserDetailsService;
import com.cookpad.security.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class JWTVerificationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final AppConfig appConfig;
    private final ObjectMapper mapper;
    public static final String URI = "uri=";

    @Autowired
    public JWTVerificationFilter(CustomUserDetailsService userDetailsService,
                                 JWTTokenProvider tokenProvider,
                                 AppConfig appConfig,
                                 ObjectMapper mapper) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.appConfig = appConfig;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        log.info("Request URL: " + request.getRequestURL());
        try {

            if (!request.getRequestURI().equals(appConfig.getRegisterEndpoint())) {
                String authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                        .orElseThrow(() -> new RecipeAPIException(HttpStatus.FORBIDDEN, "Authorization header not found."));

                Cookie accessTokenFromCookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .filter(cookie -> appConfig.getCookieName().equals(cookie.getName()))
                        .findAny()
                        .orElseThrow(() -> new RecipeAPIException(HttpStatus.FORBIDDEN, "Login cookie not found."));

                String tokenPrefix = appConfig.getTokenPrefix();
                String accessTokenFromAuthHeader = authorizationHeader.replace(tokenPrefix, "").trim();

                if (accessTokenFromCookie.getValue().equals(accessTokenFromAuthHeader)) {
                    if (tokenProvider.isValidToken(accessTokenFromAuthHeader)) {

                        // Get username from token and load user details from user service
                        String username = tokenProvider.getUsernameFromToken(accessTokenFromAuthHeader);
                        log.info("username: {}", username);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        log.info("User Details: {}", userDetails);

                        // Create and set authentication token using user details and authorities
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }

            // Go to next filter in chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            ErrorDetailsResponse errResponse = new ErrorDetailsResponse(
                    LocalDateTime.now(), e.getMessage(), URI + request.getRequestURI());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            mapper.writeValue(response.getWriter(), errResponse);
        }
    }
}
