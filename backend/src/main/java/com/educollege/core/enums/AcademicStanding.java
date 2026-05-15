package com.educollege.core.enums;

/**
 * Academic Standing Enumeration
 */
public enum AcademicStanding {
    GOOD("Tốt"),
    PROBATION("Cảnh báo"),
    SUSPENDED("Đình chỉ");
    
    private final String displayName;
    
    AcademicStanding(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
