package com.chatbot.core.academic.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Complete Session Response DTO
 */
@Data
public class CompleteSessionResponse {
    
    private Long id;
    private Long sessionId;
    
    private String completionStatus; // SATISFACTORY, NEEDS_IMPROVEMENT, EXCELLENT
    
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
