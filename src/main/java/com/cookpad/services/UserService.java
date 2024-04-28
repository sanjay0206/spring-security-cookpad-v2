package com.cookpad.services;

import com.cookpad.dto.UserDto;
import com.cookpad.entities.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long userId);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
    UserDto mapToDTO (User user);
    User mapToEntity(UserDto userDto);

}

