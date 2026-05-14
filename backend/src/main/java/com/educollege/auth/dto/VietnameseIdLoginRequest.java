package com.educollege.auth.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * Vietnamese ID Login Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VietnameseIdLoginRequest {
    
    @NotBlank(message = "Vietnamese ID cannot be blank")
    @Size(min = 8, max = 12, message = "Vietnamese ID must be between 8 and 12 characters")
    private String vietnameseId;
    
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
        if (vietnameseId == null || vietnameseId.trim().isEmpty()) {
            return false;
        }
        
        // Check if it's a student ID or teacher ID
        boolean isValidStudentId = vietnameseId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$");
        boolean isValidTeacherId = vietnameseId.matches("^GV[A-Z]{4}[0-9]{4}$");
        
        return isValidStudentId || isValidTeacherId;
    }
    
    public boolean isStudentId() {
        return vietnameseId != null && vietnameseId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$");
    }
    
    public boolean isTeacherId() {
        return vietnameseId != null && vietnameseId.matches("^GV[A-Z]{4}[0-9]{4}$");
    }
}
