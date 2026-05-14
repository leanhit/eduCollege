package com.educollege.academic.model;
import com.educollege.user.model.Teacher;


import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.educollege.core.enums.CourseOfferingStatus;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Course Offering Entity
 */
@Entity
@Table(name = "course_offerings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffering {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @Column(nullable = false)
    private Integer maxStudents;
    
    @Builder.Default
    @Column(name = "current_students")
    private Integer currentStudents = 0;
    
    @Column(name = "schedule", columnDefinition = "JSONB")
    private String schedule; // JSON schedule information
    
    @Column(length = 50)
    private String classroom;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "exam_date")
    private LocalDate examDate;
    
    @Column(name = "exam_room", length = 50)
    private String examRoom;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseOfferingStatus status; // SCHEDULED, OPEN, CLOSED, CANCELLED
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
