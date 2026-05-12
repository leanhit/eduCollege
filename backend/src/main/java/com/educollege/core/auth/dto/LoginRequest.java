package com.educollege.core.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String username;
    
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    
    // Helper method to get the actual username (supports email field)
    // Username is unique, email is used as fallback
    public String getActualUsername() {
        if (username != null && !username.trim().isEmpty()) {
            return username.trim();
        } else if (email != null && !email.trim().isEmpty()) {
            return email.trim();
        } else {
            return null;
        }
    }
    
    // Validation method to ensure at least one identifier is provided
    public boolean hasValidIdentifier() {
        return (username != null && !username.trim().isEmpty()) || 
               (email != null && !email.trim().isEmpty());
    }
}
