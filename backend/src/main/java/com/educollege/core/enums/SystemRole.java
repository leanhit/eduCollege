package com.educollege.core.enums;

/**
 * System Role Enumeration
 */
public enum SystemRole {
    ADMIN("Quản trị viên"),
    TEACHER("Giảng viên"),
    STUDENT("Sinh viên"),
    STAFF("Nhân viên"),
    DEPARTMENT_HEAD("Trưởng bộ môn"),
    FACULTY_DEAN("Trưởng khoa"),
    REGISTRAR("Phòng Đào tạo"),
    LIBRARIAN("Thư viện");
    
    private final String displayName;
    
    SystemRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
