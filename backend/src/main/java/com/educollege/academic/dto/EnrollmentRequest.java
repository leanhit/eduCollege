package com.educollege.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Enrollment Request DTO
 */
@Data
public class EnrollmentRequest {
    
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    
    @NotNull(message = "Course offering ID cannot be null")
    private Long courseOfferingId;
    
    private String enrollmentType; // REGULAR, AUDIT, RETAKE
    
    private LocalDateTime enrollmentDate;
    
    private String notes;
}
