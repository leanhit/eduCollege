package com.chatbot.core.academic.filter;

import com.chatbot.core.academic.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT Authentication Filter for Vietnamese Academic System
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Validate token
                if (jwtService.isTokenValid(jwt)) {
                    // Create user details
                    UserDetails userDetails = createUserDetailsFromToken(jwt);
                    
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("JWT authentication successful for user: {}", username);
                } else {
                    log.debug("JWT token validation failed for user: {}", username);
                }
            } catch (Exception e) {
                log.error("JWT authentication error: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Create UserDetails from JWT token
     */
    private UserDetails createUserDetailsFromToken(String token) {
        String username = jwtService.extractUsername(token);
        String vietnameseId = jwtService.extractVietnameseId(token);
        String role = jwtService.extractRole(token);
        String idCategory = jwtService.extractIdCategory(token);
        
        // Create user details with authorities based on role
        return User.builder()
            .username(username)
            .password("") // No password needed for JWT authentication
            .authorities(getAuthorities(role, idCategory))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
    /**
     * Get authorities based on role and ID category
     */
    private java.util.List<org.springframework.security.core.GrantedAuthority> getAuthorities(String role, String idCategory) {
        java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
        
        // Add role-based authority
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role));
        
        // Add category-based authorities
        if ("SINHVIEN".equals(idCategory)) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_STUDENT"));
        } else if ("GIAOVIEN".equals(idCategory)) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_TEACHER"));
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_FACULTY"));
        } else if ("NHANVIEN".equals(idCategory)) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_STAFF"));
        }
        
        // Add system-level authorities for admin
        if ("ADMIN".equals(role)) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SYSTEM"));
        }
        
        return authorities;
    }
    
    /**
     * Check if request should be skipped from JWT validation
     */
    private boolean shouldSkipValidation(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Skip JWT validation for public endpoints
        return path.startsWith("/api/v1/auth/register") ||
               path.startsWith("/api/v1/auth/login") ||
               path.startsWith("/api/v1/auth/refresh") ||
               path.startsWith("/api/v1/auth/validate") ||
               path.startsWith("/api/v1/auth/check-") ||
               path.startsWith("/api/v1/public/") ||
               path.startsWith("/actuator/health") ||
               path.startsWith("/actuator/info") ||
               path.equals("/error");
    }
    
    /**
     * Handle JWT validation errors
     */
    private void handleJwtError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        String errorResponse = String.format(
            "{\"error\": \"JWT Error\", \"message\": \"%s\", \"timestamp\": \"%s\"}",
            message,
            java.time.LocalDateTime.now()
        );
        
        response.getWriter().write(errorResponse);
    }
    
    /**
     * Add CORS headers for unauthorized responses
     */
    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
