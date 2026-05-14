package com.educollege.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Class Group Entity
 */
@Entity
@Table(name = "class_groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code; // "DH21CNTT01"
    
    @Column(nullable = false, length = 200)
    private String name; // "Lớp DH21CNTT01"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @Column(name = "enrollment_year", nullable = false)
    private Integer enrollmentYear;
    
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    @Column(name = "max_students")
    private Integer maxStudents;
    
    @Builder.Default
    @Column(name = "current_students")
    private Integer currentStudents = 0;
    
    @Column(name = "advisor_id")
    private Long advisorId;
    
    @Column(name = "classroom", length = 50)
    private String classroom;
    
    @Column(name = "schedule", columnDefinition = "TEXT")
    private String schedule;
    
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
