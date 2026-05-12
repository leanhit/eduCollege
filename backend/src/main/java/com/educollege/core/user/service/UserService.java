package com.educollege.core.user.service;

import com.educollege.core.idmanagement.IdManagementService;
import com.educollege.core.user.dto.RegisterRequest;
import com.educollege.core.user.dto.RegisterResponse;
import com.educollege.core.user.model.User;
import com.educollege.core.exception.UserException;
import com.educollege.core.user.model.UserRole;
import com.educollege.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdManagementService idManagementService;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw UserException.passwordsNotMatch();
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw UserException.usernameExists(request.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw UserException.emailExists(request.getEmail());
        }

        // Require and validate ID key - no auto-generation
        String providedIdKey = request.getIdKey();
        String idType = null;
        UserRole determinedRole = null;
        
        // ID key is required
        if (providedIdKey == null || providedIdKey.trim().isEmpty()) {
            throw UserException.invalidIdKey("ID key is required for registration");
        }
        
        // Check if ID key already exists
        if (userRepository.existsByIdKey(providedIdKey)) {
            throw UserException.idKeyExists(providedIdKey);
        }
        
        // Determine role from ID key prefix
        if (providedIdKey.startsWith("STU")) {
            determinedRole = UserRole.STUDENT;
            idType = "STUDENT_ID";
        } else if (providedIdKey.startsWith("FAC")) {
            determinedRole = UserRole.FACULTY;
            idType = "FACULTY_ID";
        } else if (providedIdKey.startsWith("EMP")) {
            determinedRole = UserRole.EMPLOYEE;
            idType = "EMPLOYEE_ID";
        } else if (providedIdKey.startsWith("STA")) {
            determinedRole = UserRole.STAFF;
            idType = "STAFF_ID";
        } else {
            throw UserException.invalidIdKey("Invalid ID key format. Must start with STU, FAC, EMP, or STA.");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(determinedRole.name())
                .status("ACTIVE")
                .emailVerified(false)
                .accountNonLocked(true)
                .idKey(providedIdKey)
                .idType(idType)
                .build();

        User savedUser = userRepository.save(user);

        log.info("Successfully registered user: {}", savedUser.getUsername());

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .status(savedUser.getStatus())
                .idKey(savedUser.getIdKey())
                .idType(savedUser.getIdType())
                .message("User registered successfully")
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> UserException.userNotFound(username));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserException.userNotFound(email));
    }

    @Transactional(readOnly = true)
    public User getUserByIdKey(String idKey) {
        return userRepository.findByIdKey(idKey)
                .orElseThrow(() -> UserException.userNotFound(idKey));
    }

    @Transactional
    public User updateUserLastLogin(String username) {
        User user = getUserByUsername(username);
        user.setLastLoginAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isIdKeyAvailable(String idKey) {
        return !userRepository.existsByIdKey(idKey);
    }

    @Transactional(readOnly = true)
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role.name());
    }

    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
}
