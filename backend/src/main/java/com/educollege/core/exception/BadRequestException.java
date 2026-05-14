package com.educollege.core.exception;

/**
 * Bad Request Exception
 * Thrown when a request is invalid
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String resourceName, String reason) {
        super(String.format("Invalid %s: %s", resourceName, reason));
    }
}
