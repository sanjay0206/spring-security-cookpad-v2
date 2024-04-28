package com.cookpad.dto;

import com.cookpad.entities.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private Long recipeId;

    @NotEmpty(message = "Username should not be null or empty")
    private String username;

    @NotEmpty(message = "Email should not be null or empty")
    @Email()
    private String email;

    private String password;

    private Gender gender;
}
