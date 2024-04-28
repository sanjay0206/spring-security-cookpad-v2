package com.cookpad.services;

import com.cookpad.dto.RegisterDto;
import com.cookpad.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    UserDto register (RegisterDto registerDto);

    String logout (HttpServletRequest request, HttpServletResponse response);
}