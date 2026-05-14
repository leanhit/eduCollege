package com.educollege.user.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Student Advisor Assignment Response DTO
 */
@Data
public class StudentAdvisorResponse {
    
    private Long studentId;
    private String studentName;
    private String studentCode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignmentDate;
    
    private Long advisorId;
    private String advisorName;
    private String advisorTitle;
    private String advisorDepartment;
    
    private String status; // ASSIGNED, PENDING, REJECTED
    
    private String notes;
}
