package com.chatbot.core.academic.controller;

import com.chatbot.core.academic.dto.LoginRequest;
import com.chatbot.core.academic.dto.StudentRegistrationRequest;
import com.chatbot.core.academic.dto.TeacherRegistrationRequest;
import com.chatbot.core.academic.dto.UserResponse;
import com.chatbot.core.academic.dto.VietnameseIdLoginRequest;
import com.chatbot.core.academic.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Authentication Controller for Vietnamese Academic System
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthenticationService authenticationService;
    
    /**
     * Register new student
     */
    @PostMapping("/register/student")
    public ResponseEntity<UserResponse> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        System.out.println("Student registration request: " + request.getEmail());
        UserResponse response = authenticationService.registerStudent(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Register new teacher
     */
    @PostMapping("/register/teacher")
    public ResponseEntity<UserResponse> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        System.out.println("Teacher registration request: " + request.getEmail());
        UserResponse response = authenticationService.registerTeacher(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Login with username or Vietnamese ID
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("Login request: " + request.getUsernameOrVietnameseId());
        UserResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Login with Vietnamese ID
     */
    @PostMapping("/login/vietnamese-id")
    public ResponseEntity<UserResponse> loginByVietnameseId(@Valid @RequestBody VietnameseIdLoginRequest request) {
        System.out.println("Vietnamese ID login request: " + request.getVietnameseId());
        UserResponse response = authenticationService.loginByVietnameseId(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<UserResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        System.out.println("Token refresh request");
        UserResponse response = authenticationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Validate token
     */
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest request) {
        System.out.println("Token validation request");
        Boolean isValid = authenticationService.validateToken(request.getToken());
        UserResponse user = null;
        
        if (isValid) {
            user = authenticationService.getUserFromToken(request.getToken());
        }
        
        TokenValidationResponse response = TokenValidationResponse.builder()
            .valid(isValid)
            .user(user)
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get current user info
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        UserResponse user = authenticationService.getUserFromToken(token);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Logout (client-side token invalidation)
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        System.out.println("Logout request for token: " + token.substring(0, Math.min(10, token.length())) + "...");
        
        // In production, add token to blacklist or invalidate refresh token
        // For now, just acknowledge logout
        
        LogoutResponse response = LogoutResponse.builder()
            .message("Logout successful")
            .timestamp(java.time.LocalDateTime.now())
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check Vietnamese ID availability
     */
    @GetMapping("/check-vietnamese-id/{vietnameseId}")
    public ResponseEntity<VietnameseIdAvailabilityResponse> checkVietnameseIdAvailability(@PathVariable String vietnameseId) {
        System.out.println("Checking Vietnamese ID availability: " + vietnameseId);
        
        boolean isAvailable = !vietnameseIdExists(vietnameseId);
        boolean isValid = isValidVietnameseIdFormat(vietnameseId);
        
        VietnameseIdAvailabilityResponse response = VietnameseIdAvailabilityResponse.builder()
            .vietnameseId(vietnameseId)
            .available(isAvailable)
            .valid(isValid)
            .message(isValid ? (isAvailable ? "Vietnamese ID is available" : "Vietnamese ID is already taken") : "Invalid Vietnamese ID format")
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check email availability
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<EmailAvailabilityResponse> checkEmailAvailability(@PathVariable String email) {
        System.out.println("Checking email availability: " + email);
        
        boolean isAvailable = !emailExists(email);
        boolean isValid = isValidEmailFormat(email);
        
        EmailAvailabilityResponse response = EmailAvailabilityResponse.builder()
            .email(email)
            .available(isAvailable)
            .valid(isValid)
            .message(isValid ? (isAvailable ? "Email is available" : "Email is already registered") : "Invalid email format")
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    // DTO classes for request/response
    
    public static class RefreshTokenRequest {
        private String refreshToken;
        
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
    
    public static class TokenValidationRequest {
        private String token;
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
    
    public static class TokenValidationResponse {
        private Boolean valid;
        private UserResponse user;
        
        public static TokenValidationResponseBuilder builder() {
            return new TokenValidationResponseBuilder();
        }
        
        public Boolean getValid() { return valid; }
        public void setValid(Boolean valid) { this.valid = valid; }
        public UserResponse getUser() { return user; }
        public void setUser(UserResponse user) { this.user = user; }
        
        public static class TokenValidationResponseBuilder {
            private Boolean valid;
            private UserResponse user;
            
            public TokenValidationResponseBuilder valid(Boolean valid) { this.valid = valid; return this; }
            public TokenValidationResponseBuilder user(UserResponse user) { this.user = user; return this; }
            
            public TokenValidationResponse build() {
                TokenValidationResponse response = new TokenValidationResponse();
                response.valid = this.valid;
                response.user = this.user;
                return response;
            }
        }
    }
    
    public static class LogoutResponse {
        private String message;
        private java.time.LocalDateTime timestamp;
        
        public static LogoutResponseBuilder builder() {
            return new LogoutResponseBuilder();
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; }
        
        public static class LogoutResponseBuilder {
            private String message;
            private java.time.LocalDateTime timestamp;
            
            public LogoutResponseBuilder message(String message) { this.message = message; return this; }
            public LogoutResponseBuilder timestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
            
            public LogoutResponse build() {
                LogoutResponse response = new LogoutResponse();
                response.message = this.message;
                response.timestamp = this.timestamp;
                return response;
            }
        }
    }
    
    public static class VietnameseIdAvailabilityResponse {
        private String vietnameseId;
        private boolean available;
        private boolean valid;
        private String message;
        
        public static VietnameseIdAvailabilityResponseBuilder builder() {
            return new VietnameseIdAvailabilityResponseBuilder();
        }
        
        public String getVietnameseId() { return vietnameseId; }
        public void setVietnameseId(String vietnameseId) { this.vietnameseId = vietnameseId; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public static class VietnameseIdAvailabilityResponseBuilder {
            private String vietnameseId;
            private boolean available;
            private boolean valid;
            private String message;
            
            public VietnameseIdAvailabilityResponseBuilder vietnameseId(String vietnameseId) { this.vietnameseId = vietnameseId; return this; }
            public VietnameseIdAvailabilityResponseBuilder available(boolean available) { this.available = available; return this; }
            public VietnameseIdAvailabilityResponseBuilder valid(boolean valid) { this.valid = valid; return this; }
            public VietnameseIdAvailabilityResponseBuilder message(String message) { this.message = message; return this; }
            
            public VietnameseIdAvailabilityResponse build() {
                VietnameseIdAvailabilityResponse response = new VietnameseIdAvailabilityResponse();
                response.vietnameseId = this.vietnameseId;
                response.available = this.available;
                response.valid = this.valid;
                response.message = this.message;
                return response;
            }
        }
    }
    
    public static class EmailAvailabilityResponse {
        private String email;
        private boolean available;
        private boolean valid;
        private String message;
        
        public static EmailAvailabilityResponseBuilder builder() {
            return new EmailAvailabilityResponseBuilder();
        }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public static class EmailAvailabilityResponseBuilder {
            private String email;
            private boolean available;
            private boolean valid;
            private String message;
            
            public EmailAvailabilityResponseBuilder email(String email) { this.email = email; return this; }
            public EmailAvailabilityResponseBuilder available(boolean available) { this.available = available; return this; }
            public EmailAvailabilityResponseBuilder valid(boolean valid) { this.valid = valid; return this; }
            public EmailAvailabilityResponseBuilder message(String message) { this.message = message; return this; }
            
            public EmailAvailabilityResponse build() {
                EmailAvailabilityResponse response = new EmailAvailabilityResponse();
                response.email = this.email;
                response.available = this.available;
                response.valid = this.valid;
                response.message = this.message;
                return response;
            }
        }
    }
    
    // Helper methods
    private boolean vietnameseIdExists(String vietnameseId) {
        // This would be implemented in AuthenticationService
        // For now, return false for demo
        return false;
    }
    
    private boolean isValidVietnameseIdFormat(String vietnameseId) {
        if (vietnameseId == null) return false;
        return vietnameseId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$") || 
               vietnameseId.matches("^GV[A-Z]{4}[0-9]{4}$");
    }
    
    private boolean emailExists(String email) {
        // This would be implemented in AuthenticationService
        // For now, return false for demo
        return false;
    }
    
    private boolean isValidEmailFormat(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
