package com.educollege.academic.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Enrollment Response DTO
 */
@Data
public class EnrollmentResponse {
    
    private Long id;
    private Long studentId;
    private Long courseOfferingId;
    
    private String enrollmentType; // REGULAR, AUDIT, RETAKE
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime enrollmentDate;
    
    private String status; // ENROLLED, COMPLETED, DROPPED, WITHDRAWN
    
    private String grade;
    private Double gradePoint;
    
    private String notes;
}
