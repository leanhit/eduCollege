package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Semester Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private Integer academicYear;
    private Integer semesterNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
}
