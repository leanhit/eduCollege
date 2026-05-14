package com.educollege.core.exception;

/**
 * Enrollment Exception
 */
public class EnrollmentException extends AcademicException {
    
    public EnrollmentException(String message) {
        super(message);
    }
    
    public EnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
