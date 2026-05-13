package com.chatbot.core.academic.enums;

/**
 * Student Status Enumeration
 */
public enum StudentStatus {
    ENROLLED("Đã đăng ký"),
    GRADUATED("Đã tốt nghiệp"),
    DROPPED("Đã thôi học"),
    SUSPENDED("Đình chỉ"),
    ON_LEAVE("Nghỉ học"),
    TRANSFERRED("Chuyển trường");
    
    private final String displayName;
    
    StudentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
