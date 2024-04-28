package com.cookpad.controllers;

import com.cookpad.dto.RegisterDto;
import com.cookpad.dto.UserDto;
import com.cookpad.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register (@RequestBody @Valid RegisterDto registerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(registerDto));
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {
        String response = authService.logout(httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
