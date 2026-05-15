package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Grade Submission Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeSubmissionRequest {

    @NotNull(message = "Enrollment ID cannot be null")
    private Long enrollmentId;

    @Min(0) @Max(10)
    private Double midtermGrade;

    @Min(0) @Max(10)
    private Double finalGrade;

    @Min(0) @Max(10)
    private Double assignmentGrade;

    @Min(0) @Max(10)
    private Double participationGrade;

    private String notes;
}
