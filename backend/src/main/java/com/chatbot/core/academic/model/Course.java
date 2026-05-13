package com.chatbot.core.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Course Entity
 */
@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 30)
    private String code; // "TIN101", "TIN102"
    
    @Column(nullable = false, length = 200)
    private String name; // "Lập trình C cơ bản"
    
    @Column(name = "vietnamese_name", nullable = false, length = 200)
    private String vietnameseName; // "Lập trình C cơ bản"
    
    @Column(name = "english_name", length = 200)
    private String englishName; // "C Programming Fundamentals"
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Integer credits; // Số tín chỉ
    
    @Column(name = "theory_hours")
    private Integer theoryHours; // Số giờ lý thuyết
    
    @Column(name = "practice_hours")
    private Integer practiceHours; // Số giờ thực hành
    
    @Column(name = "self_study_hours")
    private Integer selfStudyHours; // Số giờ tự học
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @Column(name = "prerequisites", columnDefinition = "TEXT")
    private String prerequisites; // Mã môn học tiên quyết
    
    @Column(name = "corequisites", columnDefinition = "TEXT")
    private String corequisites; // Mã môn học song hành
    
    @Column(name = "is_elective", nullable = false)
    private Boolean isElective; // Môn tự chọn
    
    @Column(name = "max_students")
    private Integer maxStudents; // Số sinh viên tối đa
    
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
