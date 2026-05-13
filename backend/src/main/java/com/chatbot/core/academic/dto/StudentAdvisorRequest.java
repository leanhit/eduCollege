package com.chatbot.core.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Student Advisor Request DTO
 */
@Data
public class StudentAdvisorRequest {
    
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    
    @NotNull(message = "Advisor ID cannot be null")
    private Long advisorId;
    
    @NotBlank(message = "Request reason cannot be blank")
    private String requestReason;
    
    private String priority; // HIGH, MEDIUM, LOW
    
    private String notes;
}
