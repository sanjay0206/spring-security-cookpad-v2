package com.cookpad.dto;

import com.cookpad.entities.Role;
import com.cookpad.entities.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;

    @NotEmpty(message = "Username should not be null or empty")
    private String username;

    @NotEmpty(message = "Email should not be null or empty")
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Gender gender;

    private Role role;
}
