package com.educollege.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Teacher Allocation Request DTO
 */
@Data
public class TeacherAllocationRequest {
    
    @NotNull(message = "Teacher ID cannot be null")
    private Long teacherId;
    
    @NotNull(message = "Classroom ID cannot be null")
    private Long classroomId;
    
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;
    
    @NotNull(message = "Semester ID cannot be null")
    private Long semesterId;
    
    private String allocationType; // PRIMARY, SECONDARY
    
    private LocalDateTime allocationDate;
    
    private String notes;
}
