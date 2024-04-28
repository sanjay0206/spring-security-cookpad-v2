package com.cookpad.repositories;

import com.cookpad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

  /*  @Query(value = "SELECT * FROM users WHERE username = :username OR email = :email", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT COUNT(*) > 0 FROM users WHERE email = :email", nativeQuery = true)
    Boolean existsByEmail(String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE username = :username", nativeQuery = true)
    Boolean existsByUsername(String username);*/
}
