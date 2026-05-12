package com.educollege.core.auth;

import com.educollege.core.auth.dto.LoginRequest;
import com.educollege.core.auth.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Login successful");
        result.put("data", response);
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Logout successful");
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestParam String username) {
        var user = authenticationService.getCurrentUser(username);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("fullName", user.getFullName());
        userData.put("role", user.getRole());
        userData.put("status", user.getStatus());
        userData.put("idKey", user.getIdKey());
        userData.put("idType", user.getIdType());
        userData.put("emailVerified", user.getEmailVerified());
        userData.put("lastLoginAt", user.getLastLoginAt());
        userData.put("createdAt", user.getCreatedAt());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", userData);
        
        return ResponseEntity.ok(result);
    }
}
