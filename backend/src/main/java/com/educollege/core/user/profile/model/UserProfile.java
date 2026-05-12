package com.educollege.core.user.profile.model;

import com.educollege.core.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * User Profile Entity - Contains detailed information about system users
 * Uses bidirectional relationship with User entity
 */
@Entity
@Table(name = "user_profiles", 
    indexes = {
        @Index(name = "idx_user_profile_user", columnList = "user_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ===== Basic Information =====
    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(length = 500)
    private String avatar;

    @Column(length = 10)
    private String gender;

    @Column(name = "bio", length = 1000)
    private String bio;

    // ===== Contact Information =====
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "alternate_email", length = 255)
    private String alternateEmail;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    // ===== Academic Information =====
    @Column(name = "student_id", length = 50)
    private String studentId;

    @Column(name = "faculty_id", length = 50)
    private String facultyId;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "year_of_study", length = 20)
    private String yearOfStudy;

    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "expected_graduation_date")
    private LocalDateTime expectedGraduationDate;

    // ===== Professional Information =====
    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "office_location", length = 100)
    private String officeLocation;

    @Column(name = "office_hours", length = 200)
    private String officeHours;

    @Column(name = "research_interests", length = 1000)
    private String researchInterests;

    @Column(name = "publications", length = 2000)
    private String publications;

    @Column(name = "linkedIn_url", length = 500)
    private String linkedInUrl;

    @Column(name = "personal_website", length = 500)
    private String personalWebsite;

    // ===== Emergency Contact =====
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    // ===== Preferences =====
    @Column(name = "preferred_language", length = 50)
    private String preferredLanguage;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "notification_preferences", length = 500)
    private String notificationPreferences;

    @Column(name = "privacy_settings", length = 500)
    private String privacySettings;

    // ===== Status Information =====
    @Column(name = "profile_status", length = 20)
    @Builder.Default
    private String profileStatus = "ACTIVE";

    @Column(name = "is_profile_public", nullable = false)
    @Builder.Default
    private Boolean isProfilePublic = false;

    @Column(name = "last_profile_update")
    private LocalDateTime lastProfileUpdate;

    // ===== Audit Fields =====
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        lastProfileUpdate = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
        lastProfileUpdate = updatedAt;
    }
}
