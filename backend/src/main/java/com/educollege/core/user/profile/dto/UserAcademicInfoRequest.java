package com.educollege.core.user.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Academic Info Request DTO - For updating academic information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAcademicInfoRequest {

    @Size(max = 50, message = "Student ID must not exceed 50 characters")
    private String studentId;

    @Size(max = 50, message = "Faculty ID must not exceed 50 characters")
    private String facultyId;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Major must not exceed 100 characters")
    private String major;

    @Size(max = 20, message = "Year of study must not exceed 20 characters")
    private String yearOfStudy;

    private Double gpa;

    private LocalDateTime enrollmentDate;

    private LocalDateTime expectedGraduationDate;
}
