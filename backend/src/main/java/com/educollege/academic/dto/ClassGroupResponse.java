package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Class Group Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroupResponse {
    
    private Long id;
    private Long facultyId;
    private String facultyCode;
    private String facultyName;
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private String description;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private Integer maxStudents;
    private Integer currentStudents;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
