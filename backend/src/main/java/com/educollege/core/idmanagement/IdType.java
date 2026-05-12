package com.educollege.core.idmanagement;

public enum IdType {
    STUDENT_ID("Student ID", "^\\d{8,12}$"),
    FACULTY_ID("Faculty ID", "^FAC\\d{6,10}$"),
    EMPLOYEE_ID("Employee ID", "^EMP\\d{6,10}$"),
    STAFF_ID("Staff ID", "^STF\\d{6,10}$");
    
    private final String description;
    private final String pattern;
    
    IdType(String description, String pattern) {
        this.description = description;
        this.pattern = pattern;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public boolean isValid(String id) {
        return id != null && id.matches(pattern);
    }
}
