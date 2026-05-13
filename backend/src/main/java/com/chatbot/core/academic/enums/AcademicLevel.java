package com.chatbot.core.academic.enums;

/**
 * Academic Level Enumeration
 */
public enum AcademicLevel {
    DAIHOC("Đại học"),
    CAODANG("Cao đẳng"),
    THACSI("Thạc sĩ"),
    TIENSI("Tiến sĩ"),
    CHUYENMON("Chuyên môn"),
    LIENTHONG("Liên thông");
    
    private final String displayName;
    
    AcademicLevel(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
