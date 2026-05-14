package com.educollege.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class Group Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroupRequest {
    
    private Long facultyId;
    private Long departmentId;
    private String code;
    private String name;
    private String vietnameseName;
    private String englishName;
    private String description;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private Integer maxStudents;
}
