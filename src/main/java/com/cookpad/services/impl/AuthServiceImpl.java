package com.cookpad.services.impl;

import com.cookpad.dto.UserDto;
import com.cookpad.entities.Role;
import com.cookpad.entities.User;
import com.cookpad.exceptions.RecipeAPIException;
import com.cookpad.repositories.RoleRepository;
import com.cookpad.repositories.UserRepository;
import com.cookpad.security.AppConfig;
import com.cookpad.services.AuthService;
import com.cookpad.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;


    @Autowired
    public AuthServiceImpl(UserService userService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AppConfig appConfig) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.appConfig = appConfig;
    }

    @Override
    public UserDto register (UserDto UserDto) {
        log.info("UserDto: "  + UserDto);

        // add check for username exists in database
        if (userRepository.existsByUsername(UserDto.getUsername())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(UserDto.getEmail())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        // create user object
        User user = new User();
        user.setUsername(UserDto.getUsername());
        user.setEmail(UserDto.getEmail());
        user.setPassword(passwordEncoder.encode(UserDto.getPassword()));
        user.setGender(UserDto.getGender());

        Set<Role> roles = UserDto.getRoles().stream()
                .map(roleName -> {
                    if ("ADMIN".equals(roleName)) {
                        throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Cannot register with ADMIN role.");
                    }
                    return roleRepository
                            .findByRoleName(roleName).orElseThrow(() -> new RecipeAPIException(HttpStatus.BAD_REQUEST, roleName + " role is not found."));
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userService.mapToDTO(savedUser);
    }

    @Override
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> clientCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(appConfig.getCookieName(), cookie.getName()))
                .findAny();

        clientCookie.ifPresent(cookie -> {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    response.addHeader(HttpHeaders.COOKIE, cookie.getValue());
                    log.info("Cookie after logout: {}", cookie.getValue());
                }
        );

        SecurityContextHolder.clearContext();
        return "User logged out successfully";
    }
}
