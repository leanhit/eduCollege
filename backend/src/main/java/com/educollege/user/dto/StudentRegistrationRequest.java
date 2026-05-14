package com.educollege.user.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Student Registration Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationRequest {
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
    
    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
    
    @NotNull(message = "Faculty ID cannot be null")
    private Long facultyId;
    
    @NotNull(message = "Class ID cannot be null")
    private Long classId;
    
    @NotNull(message = "Enrollment year cannot be null")
    @Min(value = 2000, message = "Enrollment year must be 2000 or later")
    @Max(value = 2100, message = "Enrollment year must be 2100 or earlier")
    private Integer enrollmentYear;
    
    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Vietnamese name cannot be blank")
    @Size(min = 2, max = 100, message = "Vietnamese name must be between 2 and 100 characters")
    private String vietnameseName;
    
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Invalid Vietnamese phone number format")
    private String phone;
    
    @NotBlank(message = "Date of birth cannot be blank")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Place of birth cannot be blank")
    @Size(min = 2, max = 100, message = "Place of birth must be between 2 and 100 characters")
    private String placeOfBirth;
    
    @NotBlank(message = "Address cannot be blank")
    @Size(min = 10, max = 200, message = "Address must be between 10 and 200 characters")
    private String address;
    
    @NotBlank(message = "ID number cannot be blank")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Invalid Vietnamese ID number format")
    private String idNumber;
    
    @NotBlank(message = "Academic level cannot be blank")
    @Pattern(regexp = "^(DAIHOC|CAODANG|THACSI|TIENSI)$", message = "Invalid academic level")
    private String academicLevel;
    
    private String major;
    
    private String notes;
    
    // Vietnamese specific fields
    @Builder.Default
    private String nationality = "Việt Nam";
    
    private String ethnicity;
    
    private String religion;
    
    private String guardianName;
    
    private String guardianPhone;
    
    private String guardianAddress;
    
    private String highSchool;
    
    private String graduationYear;
    
    private String admissionType; // Regular, Transfer, Articulation
    
    private String scholarshipType;
    
    private String disabilityStatus;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private String emergencyRelationship;
    
    // Validation method
    public boolean isValid() {
        if (!password.equals(confirmPassword)) {
            return false;
        }
        
        // Validate age (should be between 16 and 35 for typical university enrollment)
        if (dateOfBirth != null) {
            int age = java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
            if (age < 16 || age > 35) {
                return false;
            }
        }
        
        return true;
    }
}
