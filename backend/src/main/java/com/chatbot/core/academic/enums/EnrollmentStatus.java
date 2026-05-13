package com.chatbot.core.academic.enums;

/**
 * Enrollment Status Enumeration
 */
public enum EnrollmentStatus {
    ENROLLED("Đã đăng ký"),
    COMPLETED("Đã hoàn thành"),
    FAILED("Thất bại"),
    DROPPED("Đã hủy"),
    IN_PROGRESS("Đang học"),
    WITHDRAWN("Rút lui");
    
    private final String displayName;
    
    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
