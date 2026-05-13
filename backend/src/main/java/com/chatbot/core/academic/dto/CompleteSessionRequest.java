package com.chatbot.core.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Complete Session Request DTO
 */
@Data
public class CompleteSessionRequest {
    
    @NotNull(message = "Session ID cannot be null")
    private Long sessionId;
    
    private String completionStatus; // SATISFACTORY, NEEDS_IMPROVEMENT, EXCELLENT
    
    private String notes;
    
    private LocalDateTime completionDate;
}
