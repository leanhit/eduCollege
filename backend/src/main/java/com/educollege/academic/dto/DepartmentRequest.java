package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Department Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    
    private Long facultyId;
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private String description;
    private String contactPhone;
    private String contactEmail;
    private String officeLocation;
}
