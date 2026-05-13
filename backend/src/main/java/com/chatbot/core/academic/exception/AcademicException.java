package com.chatbot.core.academic.exception;

/**
 * Base Academic Exception
 */
public class AcademicException extends RuntimeException {
    
    public AcademicException(String message) {
        super(message);
    }
    
    public AcademicException(String message, Throwable cause) {
        super(message, cause);
    }
}
