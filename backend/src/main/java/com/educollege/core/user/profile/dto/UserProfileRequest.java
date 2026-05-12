package com.educollege.core.user.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Profile Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {

    // Basic Information
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    private LocalDateTime dateOfBirth;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    private String avatar;

    @Size(max = 10, message = "Gender must not exceed 10 characters")
    private String gender;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    // Contact Information
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Alternate email must not exceed 255 characters")
    private String alternateEmail;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    // Academic Information
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

    // Professional Information
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;

    @Size(max = 100, message = "Office location must not exceed 100 characters")
    private String officeLocation;

    @Size(max = 200, message = "Office hours must not exceed 200 characters")
    private String officeHours;

    @Size(max = 1000, message = "Research interests must not exceed 1000 characters")
    private String researchInterests;

    @Size(max = 2000, message = "Publications must not exceed 2000 characters")
    private String publications;

    @Size(max = 500, message = "LinkedIn URL must not exceed 500 characters")
    private String linkedInUrl;

    @Size(max = 500, message = "Personal website must not exceed 500 characters")
    private String personalWebsite;

    // Emergency Contact
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;

    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    private String emergencyContactPhone;

    @Size(max = 50, message = "Emergency contact relationship must not exceed 50 characters")
    private String emergencyContactRelationship;

    // Preferences
    @Size(max = 50, message = "Preferred language must not exceed 50 characters")
    private String preferredLanguage;

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    private String timezone;

    @Size(max = 500, message = "Notification preferences must not exceed 500 characters")
    private String notificationPreferences;

    @Size(max = 500, message = "Privacy settings must not exceed 500 characters")
    private String privacySettings;

    // Status Information
    @Size(max = 20, message = "Profile status must not exceed 20 characters")
    private String profileStatus;

    private Boolean isProfilePublic;
}
