package com.cookpad.services.impl;

import com.cookpad.dto.UserDto;
import com.cookpad.entities.Role;
import com.cookpad.entities.User;
import com.cookpad.exceptions.RecipeAPIException;
import com.cookpad.exceptions.ResourceNotFoundException;
import com.cookpad.repositories.RoleRepository;
import com.cookpad.repositories.UserRepository;
import com.cookpad.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

        // add check for username exists in database
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        // create user object
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setGender(userDto.getGender());

        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RecipeAPIException(HttpStatus.BAD_REQUEST, roleName + " role is not found.")))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("userId: "  + userId);
        log.info("userDto: "  + userDto);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if (userDto.getUsername() != null)
           existingUser.setUsername(userDto.getUsername());

        if (userDto.getEmail() != null)
            existingUser.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null)
            existingUser.setPassword(userDto.getPassword());

        if (userDto.getGender() != null)
            existingUser.setGender(userDto.getGender());

        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RecipeAPIException(HttpStatus.BAD_REQUEST, roleName + " role is not found.")))
                .collect(Collectors.toSet());

        if (!roles.isEmpty())
            existingUser.setRoles(roles);

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
