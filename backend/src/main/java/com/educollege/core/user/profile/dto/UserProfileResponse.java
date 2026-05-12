package com.educollege.core.user.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Profile Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private String role;

    // Basic Information
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String avatar;
    private String gender;
    private String bio;

    // Contact Information
    private String phoneNumber;
    private String alternateEmail;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    // Academic Information
    private String studentId;
    private String facultyId;
    private String department;
    private String major;
    private String yearOfStudy;
    private Double gpa;
    private LocalDateTime enrollmentDate;
    private LocalDateTime expectedGraduationDate;

    // Professional Information
    private String jobTitle;
    private String officeLocation;
    private String officeHours;
    private String researchInterests;
    private String publications;
    private String linkedInUrl;
    private String personalWebsite;

    // Emergency Contact
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;

    // Preferences
    private String preferredLanguage;
    private String timezone;
    private String notificationPreferences;
    private String privacySettings;

    // Status Information
    private String profileStatus;
    private Boolean isProfilePublic;
    private LocalDateTime lastProfileUpdate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
