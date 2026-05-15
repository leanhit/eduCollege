package com.educollege.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Sequence Entity for tracking ID generation sequences
 */
@Entity
@Table(name = "id_sequences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sequence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String sequenceKey; // "STUDENT_CNTT_DH21CNTT01_2024", "TEACHER_CNPM"
    
    @Column(nullable = false)
    private Long currentValue;
    
    @Column(name = "faculty_id")
    private Long facultyId;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "class_group_id")
    private Long classGroupId;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "sequence_type", length = 20)
    private String sequenceType; // "STUDENT", "TEACHER", "STAFF"
    
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
        if (currentValue == null) {
            currentValue = 0L;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
