package com.educollege.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduCollegeException.class)
    public ResponseEntity<Map<String, Object>> handleEduCollegeException(EduCollegeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", e.getErrorCode());
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        HttpStatus status = getHttpStatusByErrorCode(e.getErrorCode());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", "VALIDATION_ERROR");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", "AUTHENTICATION_FAILED");
        response.put("message", "Authentication failed: " + e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", "INVALID_CREDENTIALS");
        response.put("message", "Invalid username or password");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", "ACCESS_DENIED");
        response.put("message", "Access denied: " + e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", "INTERNAL_SERVER_ERROR");
        response.put("message", "An unexpected error occurred");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private HttpStatus getHttpStatusByErrorCode(String errorCode) {
        switch (errorCode) {
            case "USER_NOT_FOUND":
                return HttpStatus.NOT_FOUND;
            case "USERNAME_EXISTS":
            case "EMAIL_EXISTS":
            case "ID_KEY_EXISTS":
            case "INVALID_ID_KEY":
            case "PASSWORDS_NOT_MATCH":
                return HttpStatus.BAD_REQUEST;
            case "INVALID_CREDENTIALS":
            case "ACCOUNT_LOCKED":
            case "ACCOUNT_DISABLED":
                return HttpStatus.UNAUTHORIZED;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
