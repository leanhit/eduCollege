package com.educollege.academic.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Offering Response DTO
 */
@Data
public class CourseOfferingResponse {
    
    private Long id;
    private Long courseId;
    private String courseCode;
    private String courseName;
    
    private Long semesterId;
    private String semesterName;
    
    private Long instructorId;
    private String instructorName;
    
    private Integer maxStudents;
    private Integer currentStudents;
    
    private String schedule;
    
    private String status; // SCHEDULED, CANCELLED, COMPLETED
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private String notes;
}
