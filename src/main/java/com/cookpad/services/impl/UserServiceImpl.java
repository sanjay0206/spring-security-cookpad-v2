package com.cookpad.services.impl;

import com.cookpad.dto.UserDto;
import com.cookpad.entities.Role;
import com.cookpad.entities.User;
import com.cookpad.exceptions.ResourceNotFoundException;
import com.cookpad.repositories.UserRepository;
import com.cookpad.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> mapToDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("userId: "  + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return mapToDTO(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("userDto: "  + userDto);

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(mapToEntity(userDto));

        return mapToDTO(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("useId: "  + userId);
        log.info("userDto: "  + userDto);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());
        existingUser.setGender(userDto.getGender());

//        Role role = new Role(userDto.getRole());
//        existingUser.setRoles(userDto.getUserRole());

        return mapToDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("userId: "+ userId );

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "userId", userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto mapToDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
