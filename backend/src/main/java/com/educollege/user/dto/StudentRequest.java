package com.educollege.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Student Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {
    
    private Long userId;
    private Long facultyId;
    private Long departmentId;
    private Long classGroupId;
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
    private String notes;
    private Boolean isActive;
}
