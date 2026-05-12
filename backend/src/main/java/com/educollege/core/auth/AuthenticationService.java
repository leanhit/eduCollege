package com.educollege.core.auth;

import com.educollege.core.auth.dto.LoginRequest;
import com.educollege.core.auth.dto.LoginResponse;
import com.educollege.core.exception.UserException;
import com.educollege.core.user.model.User;
import com.educollege.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.expiration:86400}")
    private Long jwtExpiration;

    public LoginResponse authenticate(LoginRequest request) {
        System.out.println("=== AUTHENTICATION DEBUG START ===");
        
        // Validate that at least one identifier is provided
        if (!request.hasValidIdentifier()) {
            System.out.println("ERROR: No username or email provided");
            throw UserException.invalidCredentials();
        }
        
        String actualUsername = request.getActualUsername();
        System.out.println("Username: " + actualUsername);
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password provided: " + (request.getPassword() != null ? "YES" : "NO"));
        log.info("Authenticating user: {}", actualUsername);

        try {
            System.out.println("Step 1: About to authenticate with AuthenticationManager");
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    actualUsername,
                    request.getPassword()
                )
            );
            System.out.println("Step 2: Authentication successful");

            System.out.println("Step 3: Getting user details from authentication");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("Step 4: Finding user in database");
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> UserException.userNotFound(actualUsername));
            System.out.println("Step 5: User found: " + user.getUsername() + ", Role: " + user.getRole());

            // Check account status
            System.out.println("Step 6: Checking account status");
            if (!user.getAccountNonLocked()) {
                System.out.println("Account is locked");
                throw UserException.accountLocked(user.getAccountNonLockedReason());
            }

            if (!"ACTIVE".equals(user.getStatus())) {
                System.out.println("Account is not active: " + user.getStatus());
                throw UserException.accountDisabled();
            }
            System.out.println("Step 7: Account status is good");

            // Generate JWT token
            System.out.println("Step 8: About to generate JWT token for user: " + user.getUsername());
            log.info("About to generate JWT token for user: {}", user.getUsername());
            String token = jwtService.generateToken(user.getUsername(), user.getRole());
            System.out.println("Step 9: JWT token generated successfully");
            log.info("Generated JWT token successfully for user: {}", user.getUsername());

            // Update last login
            System.out.println("Step 10: Updating last login time");
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            System.out.println("Step 11: User saved to database");

            log.info("User authenticated successfully: {}", user.getUsername());
            System.out.println("=== AUTHENTICATION DEBUG END ===");

            return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .username(user.getUsername())
                .role(user.getRole())
                .fullName(user.getFullName())
                .build();

        } catch (BadCredentialsException e) {
            System.out.println("ERROR: Bad credentials for user: " + actualUsername);
            log.warn("Invalid credentials for user: {}", actualUsername);
            throw UserException.invalidCredentials();
        } catch (AuthenticationException e) {
            System.out.println("ERROR: Authentication exception for user: " + actualUsername);
            System.out.println("Authentication exception type: " + e.getClass().getSimpleName());
            System.out.println("Authentication exception message: " + e.getMessage());
            log.error("Authentication failed for user: {}", actualUsername, e);
            throw UserException.invalidCredentials();
        } catch (Exception e) {
            System.out.println("ERROR: Unexpected error for user: " + actualUsername);
            System.out.println("Error type: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            log.error("Unexpected error during authentication for user: {}", actualUsername, e);
            log.error("Error type: {}", e.getClass().getSimpleName());
            log.error("Error message: {}", e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> UserException.userNotFound(username));
    }
}
