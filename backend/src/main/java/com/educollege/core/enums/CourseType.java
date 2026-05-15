package com.educollege.core.enums;

/**
 * Course Type Enumeration
 */
public enum CourseType {
    REQUIRED("Bắt buộc"),
    ELECTIVE("Tự chọn"),
    OPTIONAL("Khóa học khác");
    
    private final String displayName;
    
    CourseType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
