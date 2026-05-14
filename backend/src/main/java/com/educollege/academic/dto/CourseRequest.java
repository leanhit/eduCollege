package com.educollege.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Course Request DTO
 */
@Data
public class CourseRequest {
    
    @NotBlank(message = "Course code cannot be blank")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;
    
    @NotBlank(message = "Course name cannot be blank")
    @Size(max = 200, message = "Course name must not exceed 200 characters")
    private String courseName;
    
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private Integer credits;
    
    private String departmentId;
    
    @NotNull(message = "Prerequisites cannot be null")
    private List<String> prerequisites;
    
    private String status; // ACTIVE, INACTIVE
    
    private String notes;
}
