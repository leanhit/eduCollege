package com.chatbot.core.academic.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Offering Request DTO
 */
@Data
public class CourseOfferingRequest {
    
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;
    
    @NotNull(message = "Semester ID cannot be null")
    private Long semesterId;
    
    @NotNull(message = "Instructor ID cannot be null")
    private Long instructorId;
    
    private Integer maxStudents;
    
    private String schedule; // JSON string with days and time
    
    private String status; // SCHEDULED, CANCELLED, COMPLETED
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private String notes;
}
