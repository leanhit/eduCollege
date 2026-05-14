package com.educollege.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Student Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private Long facultyId;
    private String facultyCode;
    private String facultyName;
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private Long classGroupId;
    private String classGroupCode;
    private String classGroupName;
    private String studentNumber;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private Double currentGpa;
    private Double cumulativeGpa;
    private Integer totalCredits;
    private Integer completedCredits;
    private Integer failedCredits;
    private String academicStanding;
    private String studentStatus;
    private Long advisorId;
    private String advisorName;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
