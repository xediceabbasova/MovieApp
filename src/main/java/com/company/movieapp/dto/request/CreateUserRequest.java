package com.company.movieapp.dto.request;

import com.company.movieapp.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "Mail is required")
        @Email(message = "Invalid email format")
        String mail,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$",
                message = "Password must contain at least one uppercase, one lowercase, one digit, one special character, and be between 6 and 20 characters")
        String password,
        Set<Role> authorities
) {
}
