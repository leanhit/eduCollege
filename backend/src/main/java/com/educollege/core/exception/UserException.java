package com.educollege.core.exception;

public class UserException extends EduCollegeException {
    
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USERNAME_EXISTS = "USERNAME_EXISTS";
    public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
    public static final String ID_KEY_EXISTS = "ID_KEY_EXISTS";
    public static final String INVALID_ID_KEY = "INVALID_ID_KEY";
    public static final String PASSWORDS_NOT_MATCH = "PASSWORDS_NOT_MATCH";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";

    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }

    public static UserException userNotFound(String username) {
        return new UserException("User not found: " + username, USER_NOT_FOUND);
    }

    public static UserException usernameExists(String username) {
        return new UserException("Username already exists: " + username, USERNAME_EXISTS);
    }

    public static UserException emailExists(String email) {
        return new UserException("Email already exists: " + email, EMAIL_EXISTS);
    }

    public static UserException idKeyExists(String idKey) {
        return new UserException("ID key already exists: " + idKey, ID_KEY_EXISTS);
    }

    public static UserException invalidIdKey(String idKey) {
        return new UserException("Invalid ID key format: " + idKey, INVALID_ID_KEY);
    }

    public static UserException passwordsNotMatch() {
        return new UserException("Passwords do not match", PASSWORDS_NOT_MATCH);
    }

    public static UserException invalidCredentials() {
        return new UserException("Invalid username or password", INVALID_CREDENTIALS);
    }

    public static UserException accountLocked(String reason) {
        return new UserException("Account is locked: " + reason, ACCOUNT_LOCKED);
    }

    public static UserException accountDisabled() {
        return new UserException("Account is disabled", ACCOUNT_DISABLED);
    }
}
