package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Faculty Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyResponse {
    
    private Long id;
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
