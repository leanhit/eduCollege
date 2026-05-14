package com.educollege.academic.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Academic Advisor Response DTO
 */
@Data
public class AcademicAdvisorResponse {
    
    private Long id;
    private Long userId;
    
    private String title;
    private String specialization;
    
    private String officeLocation;
    private String phone;
    private String email;
    
    private Integer maxStudents;
    private Integer currentStudents;
    
    private String status; // ACTIVE, INACTIVE
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private String notes;
}
