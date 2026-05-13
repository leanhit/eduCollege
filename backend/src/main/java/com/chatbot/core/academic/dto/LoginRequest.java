package com.chatbot.core.academic.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * Login Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username or Vietnamese ID cannot be blank")
    private String usernameOrVietnameseId;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    private String captchaToken;
    
    private String deviceId;
    
    private String deviceType; // WEB, MOBILE, TABLET
    
    private String userAgent;
    
    private String ipAddress;
    
    private String location;
    
    private boolean rememberMe;
    
    // Validation method
    public boolean isValid() {
        return usernameOrVietnameseId != null && !usernameOrVietnameseId.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    public boolean isVietnameseIdLogin() {
        return usernameOrVietnameseId != null && 
               (usernameOrVietnameseId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$") ||
                usernameOrVietnameseId.matches("^GV[A-Z]{4}[0-9]{4}$"));
    }
    
    public boolean isUsernameLogin() {
        return !isVietnameseIdLogin();
    }
}
