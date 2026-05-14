package com.educollege.academic.model;
import com.educollege.user.model.Student;


import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.educollege.core.enums.EnrollmentStatus;
import java.time.LocalDateTime;

/**
 * Enrollment Entity
 */
@Entity
@Table(name = "enrollments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_offering_id", nullable = false)
    private CourseOffering courseOffering;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Column(name = "grade", columnDefinition = "DECIMAL(5,2)")
    private Double grade;
    
    @Column(name = "letter_grade", length = 2)
    private String letterGrade;
    
    @Column(name = "gpa_points", columnDefinition = "DECIMAL(3,2)")
    private Double gpaPoints;
    
    @Column(name = "attendance_rate", columnDefinition = "DECIMAL(5,2)")
    private Double attendanceRate;
    
    @Column(name = "midterm_grade", columnDefinition = "DECIMAL(5,2)")
    private Double midtermGrade;
    
    @Column(name = "final_grade", columnDefinition = "DECIMAL(5,2)")
    private Double finalGrade;
    
    @Column(name = "assignment_grade", columnDefinition = "DECIMAL(5,2)")
    private Double assignmentGrade;
    
    @Column(name = "participation_grade", columnDefinition = "DECIMAL(5,2)")
    private Double participationGrade;
    
    @Column(name = "grade_breakdown", columnDefinition = "JSONB")
    private String gradeBreakdown;
    
    @Column(name = "graded_by")
    private Long gradedBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "grade_submission_date")
    private LocalDateTime gradeSubmissionDate;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
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
        if (status == null) {
            status = EnrollmentStatus.ENROLLED;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
