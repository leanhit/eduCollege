package com.educollege.core.user.dto;

import com.educollege.core.user.model.UserRole;
import com.educollege.core.validation.ValidEmail;
import com.educollege.core.validation.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @ValidEmail
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    // Required: Pre-generated ID key (role will be auto-determined from prefix)
    @NotBlank(message = "ID key is required")
    @Size(max = 20, message = "ID key must not exceed 20 characters")
    private String idKey;

    // Role will be auto-determined from idKey prefix or default to STUDENT
    // private UserRole role;

    // Note: If idKey is provided, role will be auto-determined from its prefix
    // If idKey is empty, new STUDENT ID will be generated
}
