package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Faculty Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyRequest {
    
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private String description;
    private String contactPhone;
    private String contactEmail;
    private String officeLocation;
}
