package com.educollege.core.exception;

/**
 * Course Exception
 */
public class CourseException extends AcademicException {
    
    public CourseException(String message) {
        super(message);
    }
    
    public CourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
