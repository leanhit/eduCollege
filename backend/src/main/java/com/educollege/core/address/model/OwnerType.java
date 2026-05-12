package com.educollege.core.address.model;

public enum OwnerType {
    USER("User"),
    STUDENT("Student"),
    FACULTY("Faculty"),
    STAFF("Staff"),
    DEPARTMENT("Department"),
    COURSE("Course"),
    BUILDING("Building"),
    ROOM("Room"),
    LIBRARY("Library"),
    EQUIPMENT("Equipment");
    
    private final String displayName;
    
    OwnerType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
