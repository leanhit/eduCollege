package com.educollege.core.config;

import com.educollege.auth.service.JwtService;
import com.educollege.core.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Security Configuration for Vietnamese Academic System
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtService jwtService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/v1/auth/register/**").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/login/vietnamese-id").permitAll()
                .requestMatchers("/api/v1/auth/refresh").permitAll()
                .requestMatchers("/api/v1/auth/validate").permitAll()
                .requestMatchers("/api/v1/auth/check-vietnamese-id/**").permitAll()
                .requestMatchers("/api/v1/auth/check-email/**").permitAll()
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                
                // Academic endpoints - all authenticated users can read
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/academic/faculties/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/academic/departments/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/academic/courses/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/academic/teachers/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                
                // Student endpoints
                .requestMatchers("/api/v1/students/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                
                // Teacher endpoints
                .requestMatchers("/api/v1/teachers/**").hasAnyRole("TEACHER", "ADMIN")
                
                // Enrollment endpoints
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/academic/enrollments").hasRole("USER")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/academic/enrollments/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/academic/enrollments/*/grade").hasRole("TEACHER")
                
                // Advising endpoints
                .requestMatchers("/api/v1/academic/advising/**").hasAnyRole("TEACHER", "ADMIN")
                
                // Admin endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/academic/faculties").hasRole("ADMIN")
                .requestMatchers("/api/v1/academic/departments").hasRole("ADMIN")
                .requestMatchers("/api/v1/academic/courses").hasRole("ADMIN")
                .requestMatchers("/api/v1/academic/teachers").hasRole("ADMIN")
                
                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8080",
            "http://localhost:4200",
            "https://educollege.edu.vn",
            "https://*.educollege.edu.vn"
        ));
        
        // Allow specific HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allow specific headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Expose specific headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count"
        ));
        
        // Set max age
        configuration.setMaxAge(3600L);
        
        // Register CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    

}
