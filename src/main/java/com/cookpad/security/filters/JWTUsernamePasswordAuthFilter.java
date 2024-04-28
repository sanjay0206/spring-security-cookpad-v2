package com.cookpad.security.filters;

import com.cookpad.dto.LoginDto;
import com.cookpad.responses.JWTAccessTokenResponse;
import com.cookpad.security.AppConfig;
import com.cookpad.security.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JWTUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    protected AuthenticationManager authenticationManager;
    private final AppConfig appConfig;
    private final JWTTokenProvider tokenProvider;
    private final ObjectMapper mapper;

    @Autowired
    public JWTUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
                                         JWTTokenProvider tokenProvider,
                                         AppConfig appConfig,
                                         ObjectMapper mapper) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.appConfig = appConfig;
        this.mapper = mapper;
        setFilterProcessesUrl(appConfig.getLoginEndpoint());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        log.info("Request URL: " + request.getRequestURL());
        Authentication authentication = null;
        try {
            LoginDto loginDto = mapper.readValue(request.getInputStream(), LoginDto.class);
            log.info("Username is: {}", loginDto.getUsernameOrEmail());
            log.info("Password is: {}", loginDto.getPassword());
            authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        log.info("Auth result: {}", authResult);
        String token = tokenProvider.generateToken(authResult);
        JWTAccessTokenResponse accessTokenResponse =
                new JWTAccessTokenResponse(token, appConfig.getTokenPrefix());

        Cookie clientCookie = new Cookie(appConfig.getCookieName(), token);
        clientCookie.setMaxAge(appConfig.getCookieMaxAge());

        log.info("Cookie[name={}, value={}]", clientCookie.getName(), clientCookie.getValue());
        response.addHeader(HttpHeaders.COOKIE, token);
        response.addCookie(clientCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // send JWT token to response body
        mapper.writeValue(response.getWriter(), accessTokenResponse);
    }
}
