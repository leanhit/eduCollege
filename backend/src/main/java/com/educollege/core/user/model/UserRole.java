package com.educollege.core.user.model;

public enum UserRole {
    STUDENT("Student"),
    FACULTY("Faculty"), 
    EMPLOYEE("Employee"),
    STAFF("Staff"),
    ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
