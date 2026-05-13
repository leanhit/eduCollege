package com.chatbot.core.academic.security;

import com.chatbot.core.academic.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * User Details Service Implementation for Vietnamese Academic System
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final JwtService jwtService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);
        
        // In production, this would load user from database
        // For now, create a demo user based on username pattern
        
        if (username.startsWith("student")) {
            return createStudentUserDetails(username);
        } else if (username.startsWith("teacher")) {
            return createTeacherUserDetails(username);
        } else if (username.startsWith("admin")) {
            return createAdminUserDetails(username);
        } else {
            // Try to find user by email or Vietnamese ID
            return createGenericUserDetails(username);
        }
    }
    
    /**
     * Create student user details
     */
    private UserDetails createStudentUserDetails(String username) {
        log.debug("Creating student user details for: {}", username);
        
        return User.builder()
            .username(username)
            .password("$2a$10$demoPasswordHash") // Dummy password hash
            .authorities(createStudentAuthorities())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
    /**
     * Create teacher user details
     */
    private UserDetails createTeacherUserDetails(String username) {
        log.debug("Creating teacher user details for: {}", username);
        
        return User.builder()
            .username(username)
            .password("$2a$10$demoPasswordHash") // Dummy password hash
            .authorities(createTeacherAuthorities())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
    /**
     * Create admin user details
     */
    private UserDetails createAdminUserDetails(String username) {
        log.debug("Creating admin user details for: {}", username);
        
        return User.builder()
            .username(username)
            .password("$2a$10$demoPasswordHash") // Dummy password hash
            .authorities(createAdminAuthorities())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
    /**
     * Create generic user details
     */
    private UserDetails createGenericUserDetails(String username) {
        log.debug("Creating generic user details for: {}", username);
        
        // Default to student role for unknown usernames
        return User.builder()
            .username(username)
            .password("$2a$10$demoPasswordHash") // Dummy password hash
            .authorities(createStudentAuthorities())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
    /**
     * Create student authorities
     */
    private java.util.List<org.springframework.security.core.GrantedAuthority> createStudentAuthorities() {
        java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_STUDENT"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SINHVIEN"));
        return authorities;
    }
    
    /**
     * Create teacher authorities
     */
    private java.util.List<org.springframework.security.core.GrantedAuthority> createTeacherAuthorities() {
        java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_TEACHER"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_FACULTY"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_GIAOVIEN"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")); // Teachers are also users
        return authorities;
    }
    
    /**
     * Create admin authorities
     */
    private java.util.List<org.springframework.security.core.GrantedAuthority> createAdminAuthorities() {
        java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SYSTEM"));
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_TEACHER")); // Admin can do everything
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
    
    /**
     * Load user by Vietnamese ID
     */
    public UserDetails loadUserByVietnameseId(String vietnameseId) throws UsernameNotFoundException {
        log.debug("Loading user details by Vietnamese ID: {}", vietnameseId);
        
        // Determine user type from Vietnamese ID format
        if (vietnameseId.startsWith("SV")) {
            // Student ID format: SVYYFACULTYSEQUENCE
            return createStudentUserDetails("student_" + vietnameseId);
        } else if (vietnameseId.startsWith("GV")) {
            // Teacher ID format: GVDEPARTMENTSEQUENCE
            return createTeacherUserDetails("teacher_" + vietnameseId);
        } else {
            throw new UsernameNotFoundException("Invalid Vietnamese ID format: " + vietnameseId);
        }
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(String username) {
        // In production, check database
        // For now, return true for demo users
        return username.startsWith("student") || 
               username.startsWith("teacher") || 
               username.startsWith("admin") ||
               username.contains("@");
    }
    
    /**
     * Get user role from username
     */
    public String getUserRole(String username) {
        if (username.startsWith("admin")) {
            return "ADMIN";
        } else if (username.startsWith("teacher")) {
            return "TEACHER";
        } else {
            return "USER"; // Default to student/user
        }
    }
    
    /**
     * Get user ID category from username
     */
    public String getUserIdCategory(String username) {
        if (username.startsWith("student") || username.startsWith("SV")) {
            return "SINHVIEN";
        } else if (username.startsWith("teacher") || username.startsWith("GV")) {
            return "GIAOVIEN";
        } else if (username.startsWith("admin")) {
            return "NHANVIEN";
        } else {
            return "SINHVIEN"; // Default
        }
    }
    
    /**
     * Validate user credentials
     */
    public boolean validateUserCredentials(String username, String password) {
        // In production, verify against database
        // For now, accept "demo-password" for all demo users
        return "demo-password".equals(password);
    }
    
    /**
     * Check if user is active
     */
    public boolean isUserActive(String username) {
        // In production, check user status in database
        // For now, all demo users are active
        return true;
    }
    
    /**
     * Check if user account is locked
     */
    public boolean isUserAccountLocked(String username) {
        // In production, check account lock status in database
        // For now, no demo accounts are locked
        return false;
    }
    
    /**
     * Check if user account is expired
     */
    public boolean isUserAccountExpired(String username) {
        // In production, check account expiration in database
        // For now, no demo accounts are expired
        return false;
    }
    
    /**
     * Check if user credentials are expired
     */
    public boolean isUserCredentialsExpired(String username) {
        // In production, check credential expiration in database
        // For now, no demo credentials are expired
        return false;
    }
}
