package com.educollege.core.enums;

/**
 * Course Offering Status Enumeration
 */
public enum CourseOfferingStatus {
    SCHEDULED("Đã lên lịch"),
    OPEN("Mở đăng ký"),
    CLOSED("Đã đóng"),
    CANCELLED("Đã hủy"),
    COMPLETED("Đã kết thúc");
    
    private final String displayName;
    
    CourseOfferingStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
