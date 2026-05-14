package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Department Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    
    private Long id;
    private Long facultyId;
    private String facultyCode;
    private String facultyName;
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private String description;
    private String contactPhone;
    private String contactEmail;
    private String officeLocation;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
