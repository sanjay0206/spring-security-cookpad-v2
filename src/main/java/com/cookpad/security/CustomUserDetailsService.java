package com.cookpad.security;


import com.cookpad.entities.Role;
import com.cookpad.entities.User;
import com.cookpad.mapper.UserMapper;
import com.cookpad.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String ROLE_PREFIX = "ROLE_";
//    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        log.info("User: {}", user);

        String username = user.getUsername();
        String password = user.getPassword();
        Set<Role> roles = user.getRoles();

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .authorities(mapRolesToAuthorities(roles))
                .build();
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())).collect(Collectors.toList());
    }
}
