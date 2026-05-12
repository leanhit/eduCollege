package com.educollege.core.exception;

public class EduCollegeException extends RuntimeException {
    private final String errorCode;

    public EduCollegeException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
    }

    public EduCollegeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EduCollegeException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
    }

    public EduCollegeException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static EduCollegeException notFound(String message) {
        return new EduCollegeException(message, "NOT_FOUND");
    }

    public static EduCollegeException badRequest(String message) {
        return new EduCollegeException(message, "BAD_REQUEST");
    }

    public static EduCollegeException unauthorized(String message) {
        return new EduCollegeException(message, "UNAUTHORIZED");
    }

    public static EduCollegeException forbidden(String message) {
        return new EduCollegeException(message, "FORBIDDEN");
    }
}
