package com.educollege.user.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User Response DTO
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    
    private String email;
    
    private String username;
    
    private String vietnameseId;
    
    private String idCategory; // SINHVIEN, GIAOVIEN, NHANVIEN
    
    private String academicLevel;
    
    private String fullName;
    
    private String vietnameseName;
    
    private String phone;
    
    private String address;
    
    private String dateOfBirth;
    
    private String placeOfBirth;
    
    private String nationality;
    
    private String ethnicity;
    
    private String religion;
    
    private String systemRole;
    
    private String status; // ACTIVE, INACTIVE, SUSPENDED
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLoginAt;
    
    private String lastLoginIp;
    
    private String lastLoginDevice;
    
    private String lastLoginLocation;
    
    // Academic specific fields
    private Long facultyId;
    
    private String facultyCode;
    
    private String facultyName;
    
    private Long departmentId;
    
    private String departmentCode;
    
    private String departmentName;
    
    private Long classId;
    
    private String classCode;
    
    private String className;
    
    private Integer enrollmentYear;
    
    private Integer graduationYear;
    
    private String academicStanding;
    
    private Double currentGpa;
    
    private Double cumulativeGpa;
    
    private Integer totalCredits;
    
    private Integer completedCredits;
    
    private Integer failedCredits;
    
    private String studentStatus;
    
    private String studentNumber;
    
    private String teacherNumber;
    
    private String academicTitle;
    
    private String specialization;
    
    private String officeLocation;
    
    private String officePhone;
    
    private String researchInterests;
    
    private LocalDate hireDate;
    
    // JWT tokens
    private String token;
    
    private String refreshToken;
    
    private String tokenType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tokenExpiresAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenExpiresAt;
    
    // Permissions
    private String[] permissions;
    
    private String[] roles;
    
    // Profile image
    private String profileImageUrl;
    
    private String profileImageThumbnailUrl;
    
    // Preferences
    private String language;
    
    private String timezone;
    
    private String theme;
    
    private String notificationEmail;
    
    private String notificationSms;
    
    private boolean emailVerified;
    
    private boolean phoneVerified;
    
    private boolean twoFactorEnabled;
    
    // Security information
    private String lastPasswordChange;
    
    private String lastLoginAttempt;
    
    private Integer failedLoginAttempts;
    
    private boolean accountLocked;
    
    private LocalDateTime accountLockedUntil;
    
    // Audit information
    private String createdBy;
    
    private String updatedBy;
    
    private String ipAddress;
    
    private String userAgent;
    
    // Vietnamese specific fields
    private String idNumber;
    
    private String guardianName;
    
    private String guardianPhone;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private String emergencyRelationship;
    
    // Factory methods for demonstration (will be updated when User model is available)
    public static UserResponse createDemoUser(String email, String vietnameseId, String role) {
        return UserResponse.builder()
            .email(email)
            .vietnameseId(vietnameseId)
            .idCategory(vietnameseId.startsWith("SV") ? "SINHVIEN" : "GIAOVIEN")
            .systemRole(role)
            .status("ACTIVE")
            .token("demo-token")
            .refreshToken("demo-refresh-token")
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(86400))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(604800))
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
    }
    
    public static UserResponse fromDemo(com.educollege.user.model.Student student) {
        return UserResponse.builder()
            .id(student.getId())
            .studentNumber(student.getStudentNumber())
            .vietnameseId(student.getStudentNumber())
            .idCategory("SINHVIEN")
            .academicLevel("DAIHOC")
            .fullName("Demo Student")
            .vietnameseName("Sinh Viên Demo")
            .studentStatus(student.getStudentStatus().toString())
            .currentGpa(student.getCurrentGpa())
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
    }
    
    public static UserResponse fromDemo(com.educollege.user.model.Teacher teacher) {
        return UserResponse.builder()
            .id(teacher.getId())
            .teacherNumber(teacher.getTeacherNumber())
            .vietnameseId(teacher.getTeacherNumber())
            .idCategory("GIAOVIEN")
            .academicLevel("DAIHOC")
            .fullName("Demo Teacher")
            .vietnameseName("Giảng Viên Demo")
            .academicTitle(teacher.getAcademicTitle())
            .specialization(teacher.getSpecialization())
            .officeLocation(teacher.getOfficeLocation())
            .hireDate(teacher.getHireDate())
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
    }
}
