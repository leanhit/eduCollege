package com.educollege.core.enums;

/**
 * Payment Status Enumeration
 */
public enum PaymentStatus {
    UNPAID("Chưa thanh toán"),
    PARTIAL("Thanh toán một phần"),
    PAID("Đã thanh toán"),
    OVERDUE("Quá hạn");
    
    private final String displayName;
    
    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
