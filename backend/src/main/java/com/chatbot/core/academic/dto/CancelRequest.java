package com.chatbot.core.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Cancel Request DTO for graduation application cancellation
 */
@Data
public class CancelRequest {
    
    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;
    
    @NotBlank(message = "Reason cannot be blank")
    private String reason;
    
    private String cancelRole;
    private LocalDateTime cancelDate;
    
    private String comments;
    
    @Data
    public static class CancelRole {
        public static final String STUDENT = "STUDENT";
        public static final String ADVISOR = "ADVISOR";
        public static final String ADMIN = "ADMIN";
    }
}
