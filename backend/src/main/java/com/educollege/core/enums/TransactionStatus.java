package com.educollege.core.enums;

/**
 * Transaction Status Enumeration
 */
public enum TransactionStatus {
    SUCCESS("Thành công"),
    FAILED("Thất bại"),
    PENDING("Đang xử lý"),
    CANCELLED("Đã hủy");
    
    private final String displayName;
    
    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
