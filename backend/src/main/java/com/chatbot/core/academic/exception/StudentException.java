package com.chatbot.core.academic.exception;

/**
 * Student Exception
 */
public class StudentException extends AcademicException {
    
    public StudentException(String message) {
        super(message);
    }
    
    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
