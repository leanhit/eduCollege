package com.educollege.user.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Teacher Allocation Response DTO
 */
@Data
public class TeacherAllocationResponse {
    
    private Long teacherId;
    private String teacherName;
    private String teacherCode;
    
    private Long classroomId;
    private String roomNumber;
    private String buildingName;
    
    private Long courseId;
    private String courseName;
    
    private Long semesterId;
    private String semesterName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime allocationDate;
    
    private String allocationType; // PRIMARY, SECONDARY
    
    private String status; // ASSIGNED, PENDING, REJECTED
    
    private String notes;
}
