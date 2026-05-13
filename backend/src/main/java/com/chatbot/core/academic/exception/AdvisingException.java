package com.chatbot.core.academic.exception;

/**
 * Advising Exception
 */
public class AdvisingException extends AcademicException {
    
    public AdvisingException(String message) {
        super(message);
    }
    
    public AdvisingException(String message, Throwable cause) {
        super(message, cause);
    }
}
