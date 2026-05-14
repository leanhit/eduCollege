package com.educollege.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Academic Advisor Request DTO
 */
@Data
public class AcademicAdvisorRequest {
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;
    
    @NotBlank(message = "Specialization cannot be blank")
    @Size(max = 200, message = "Specialization must not exceed 200 characters")
    private String specialization;
    
    @NotBlank(message = "Office location cannot be blank")
    @Size(max = 100, message = "Office location must not exceed 100 characters")
    private String officeLocation;
    
    @NotBlank(message = "Phone cannot be blank")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    private Integer maxStudents;
    
    private String status; // ACTIVE, INACTIVE
    
    private String notes;
}
