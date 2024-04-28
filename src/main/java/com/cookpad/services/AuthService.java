package com.cookpad.services;


import com.cookpad.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    UserDto register (UserDto UserDto);

    String logout (HttpServletRequest request, HttpServletResponse response);
}