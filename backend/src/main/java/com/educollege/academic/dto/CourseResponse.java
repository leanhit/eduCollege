package com.educollege.academic.dto;

import lombok.Data;
import java.util.List;

/**
 * Course Response DTO
 */
@Data
public class CourseResponse {
    
    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    
    private Integer credits;
    
    private String departmentId;
    private String departmentName;
    
    private List<String> prerequisites;
    
    private String status; // ACTIVE, INACTIVE
    
    private String notes;
}
