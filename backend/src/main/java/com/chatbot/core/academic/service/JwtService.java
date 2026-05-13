package com.chatbot.core.academic.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Service for Vietnamese Academic System
 */
@Service
public class JwtService {
    
    @Value("${jwt.secret:mySecretKey}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours
    private long jwtExpiration;
    
    @Value("${jwt.refresh-expiration:604800000}") // 7 days
    private long refreshExpiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract Vietnamese ID from token
     */
    public String extractVietnameseId(String token) {
        return extractClaim(token, claims -> claims.get("vietnameseId", String.class));
    }
    
    /**
     * Extract user role from token
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    
    /**
     * Extract user category from token
     */
    public String extractIdCategory(String token) {
        return extractClaim(token, claims -> claims.get("idCategory", String.class));
    }
    
    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    /**
     * Check if token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Generate token for user
     */
    public String generateToken(String username, String vietnameseId, String role, String idCategory) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("vietnameseId", vietnameseId);
        claims.put("role", role);
        claims.put("idCategory", idCategory);
        return createToken(claims, username, jwtExpiration);
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String username, String vietnameseId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("vietnameseId", vietnameseId);
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpiration);
    }
    
    /**
     * Create token with claims and expiration
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    /**
     * Validate token against user details
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Validate token for Vietnamese ID
     */
    public Boolean validateVietnameseIdToken(String token, String vietnameseId) {
        final String extractedId = extractVietnameseId(token);
        return (extractedId.equals(vietnameseId) && !isTokenExpired(token));
    }
    
    /**
     * Check if token is valid and not expired
     */
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get token expiration time in milliseconds
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }
    
    /**
     * Get refresh token expiration time in milliseconds
     */
    public long getRefreshExpirationTime() {
        return refreshExpiration;
    }
    
    /**
     * Parse token without throwing exception
     */
    public Claims parseToken(String token) {
        try {
            return extractAllClaims(token);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Check if token is a refresh token
     */
    public Boolean isRefreshToken(String token) {
        Claims claims = parseToken(token);
        return claims != null && "refresh".equals(claims.get("type"));
    }
    
    /**
     * Generate token with custom claims
     */
    public String generateTokenWithClaims(Map<String, Object> additionalClaims, String username) {
        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("type", "access");
        return createToken(claims, username, jwtExpiration);
    }
    
    /**
     * Validate token for specific role
     */
    public Boolean validateTokenForRole(String token, String requiredRole) {
        try {
            String role = extractRole(token);
            return requiredRole.equals(role) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validate token for specific ID category
     */
    public Boolean validateTokenForIdCategory(String token, String requiredCategory) {
        try {
            String category = extractIdCategory(token);
            return requiredCategory.equals(category) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
