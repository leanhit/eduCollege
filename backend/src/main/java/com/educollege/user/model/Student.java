package com.educollege.user.model;
import com.educollege.academic.model.Faculty;
import com.educollege.academic.model.Department;
import com.educollege.academic.model.ClassGroup;


import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.educollege.core.enums.StudentStatus;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Student Entity
 */
@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "student_number", unique = true, nullable = false, length = 20)
    private String studentNumber; // "SVCNTT20240001"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id")
    private ClassGroup classGroup;
    
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;
    
    @Column(name = "expected_graduation_date")
    private LocalDate expectedGraduationDate;
    
    @Column(name = "actual_graduation_date")
    private LocalDate actualGraduationDate;
    
    @Column(name = "enrollment_year", nullable = false)
    private Integer enrollmentYear;
    
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    @Column(name = "current_gpa", columnDefinition = "DECIMAL(3,2)")
    private Double currentGpa;
    
    @Column(name = "cumulative_gpa", columnDefinition = "DECIMAL(3,2)")
    private Double cumulativeGpa;
    
    @Column(name = "total_credits")
    private Integer totalCredits;
    
    @Column(name = "completed_credits")
    private Integer completedCredits;
    
    @Column(name = "failed_credits")
    private Integer failedCredits;
    
    @Column(name = "academic_standing", length = 20)
    private String academicStanding; // GOOD, PROBATION, SUSPENDED
    
    @Column(name = "class_rank")
    private Integer classRank;
    
    @Column(name = "faculty_rank")
    private Integer facultyRank;
    
    @Column(name = "total_students_in_class")
    private Integer totalStudentsInClass;
    
    @Column(name = "total_students_in_faculty")
    private Integer totalStudentsInFaculty;
    
    @Column(name = "advisor_id")
    private Long advisorId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "student_status", nullable = false)
    private StudentStatus studentStatus;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (studentStatus == null) {
            studentStatus = StudentStatus.ENROLLED;
        }
        if (academicStanding == null) {
            academicStanding = "GOOD";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
