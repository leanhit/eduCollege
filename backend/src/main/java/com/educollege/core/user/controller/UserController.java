package com.educollege.core.user.controller;

import com.educollege.core.user.dto.RegisterRequest;
import com.educollege.core.user.dto.RegisterResponse;
import com.educollege.core.user.model.UserRole;
import com.educollege.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = userService.registerUser(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "User registered successfully");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
            
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String username) {
        boolean available = userService.isUsernameAvailable(username);
        
        Map<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("available", available);
        result.put("message", available ? "Username is available" : "Username is already taken");
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmail(@PathVariable String email) {
        boolean available = userService.isEmailAvailable(email);
        
        Map<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("available", available);
        result.put("message", available ? "Email is available" : "Email is already registered");
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-id-key/{idKey}")
    public ResponseEntity<Map<String, Object>> checkIdKey(@PathVariable String idKey) {
        boolean available = userService.isIdKeyAvailable(idKey);
        
        Map<String, Object> result = new HashMap<>();
        result.put("idKey", idKey);
        result.put("available", available);
        result.put("message", available ? "ID key is available" : "ID key is already registered");
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActiveUsers", userService.countActiveUsers());
        stats.put("totalStudents", userService.countUsersByRole(UserRole.STUDENT));
        stats.put("totalFaculty", userService.countUsersByRole(UserRole.FACULTY));
        stats.put("totalEmployees", userService.countUsersByRole(UserRole.EMPLOYEE));
        stats.put("totalStaff", userService.countUsersByRole(UserRole.STAFF));
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", stats);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        try {
            var user = userService.getUserByUsername(username);
            
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
            
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            
            return ResponseEntity.notFound().build();
        }
    }
}
