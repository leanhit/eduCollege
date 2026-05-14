package com.educollege.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Teacher Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequest {
    
    private Long userId;
    private Long departmentId;
    private String teacherNumber;
    private String academicTitle;
    private LocalDate hireDate;
    private String specialization;
    private String email;
    private String phone;
    private String officeLocation;
    private Boolean isAdvisor;
    private Integer maxAdvisees;
    private Integer currentAdvisees;
    private Integer maxCoursesPerSemester;
    private Integer currentCoursesPerSemester;
    private String notes;
    private Boolean isActive;
}
