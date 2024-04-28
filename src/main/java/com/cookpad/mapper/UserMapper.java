package com.cookpad.mapper;


import com.cookpad.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    Boolean existsByUsername(String username);
}
