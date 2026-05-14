package com.educollege.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Semester Entity
 */
@Entity
@Table(name = "semesters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code; // "20241", "20242"
    
    @Column(nullable = false, length = 100)
    private String name; // "Học kỳ 1 năm 2024"
    
    @Column(name = "academic_year", nullable = false, length = 10)
    private String academicYear; // "2024-2025"
    
    @Column(name = "semester_number", nullable = false)
    private Integer semesterNumber; // 1, 2, 3
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "registration_start")
    private LocalDate registrationStart;
    
    @Column(name = "registration_end")
    private LocalDate registrationEnd;
    
    @Column(name = "add_drop_start")
    private LocalDate addDropStart;
    
    @Column(name = "add_drop_end")
    private LocalDate addDropEnd;
    
    @Column(name = "exam_start")
    private LocalDate examStart;
    
    @Column(name = "exam_end")
    private LocalDate examEnd;
    
    @Column(name = "grade_submission_deadline")
    private LocalDate gradeSubmissionDeadline;
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
