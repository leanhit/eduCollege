package com.educollege.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Teacher Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {
    
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private String teacherNumber;
    private String academicTitle;
    private LocalDate hireDate;
    private String specialization;
    private String phone;
    private String officeLocation;
    private Boolean isAdvisor;
    private Integer maxAdvisees;
    private Integer currentAdvisees;
    private Integer maxCoursesPerSemester;
    private Integer currentCoursesPerSemester;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
