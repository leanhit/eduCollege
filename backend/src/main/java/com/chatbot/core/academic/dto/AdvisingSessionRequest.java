package com.chatbot.core.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Advising Session Request DTO
 */
@Data
public class AdvisingSessionRequest {
    
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    
    @NotNull(message = "Advisor ID cannot be null")
    private Long advisorId;
    
    @NotNull(message = "Session date cannot be null")
    private LocalDateTime sessionDate;
    
    @NotBlank(message = "Session type cannot be blank")
    private String sessionType; // ACADEMIC_PLANNING, CAREER_COUNSELING, REGISTRATION_ASSISTANCE
    
    private String notes;
    
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    
    private LocalDateTime followUpDate;
    
    private Boolean followUpRequired;
}
