package com.chatbot.core.academic.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Advising Session Response DTO
 */
@Data
public class AdvisingSessionResponse {
    
    private Long id;
    private Long studentId;
    private Long advisorId;
    
    private LocalDateTime sessionDate;
    
    private String sessionType; // ACADEMIC_PLANNING, CAREER_COUNSELING, REGISTRATION_ASSISTANCE
    
    private String notes;
    
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    
    private LocalDateTime followUpDate;
    
    private Boolean followUpRequired;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
