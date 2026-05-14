package com.educollege.user.model;
import com.educollege.academic.model.Department;


import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Teacher Entity
 */
@Entity
@Table(name = "teachers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "teacher_number", unique = true, nullable = false, length = 20)
    private String teacherNumber; // "GVCNTT0001"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @Column(name = "academic_title", length = 100)
    private String academicTitle; // "Giáo sư", "Phó Giáo sư", "Tiến sĩ", "Thạc sĩ"
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "specialization", length = 200)
    private String specialization;
    
    @Column(name = "research_interests", columnDefinition = "TEXT")
    private String researchInterests;
    
    @Column(name = "office_location", length = 200)
    private String officeLocation;
    
    @Column(name = "office_phone", length = 20)
    private String officePhone;
    
    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "max_courses_per_semester")
    private Integer maxCoursesPerSemester;
    
    @Column(name = "current_courses_per_semester")
    private Integer currentCoursesPerSemester;
    
    @Column(name = "is_advisor", nullable = false)
    private Boolean isAdvisor;
    
    @Column(name = "max_advisees")
    private Integer maxAdvisees;
    
    @Column(name = "current_advisees")
    private Integer currentAdvisees;
    
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
        if (currentCoursesPerSemester == null) {
            currentCoursesPerSemester = 0;
        }
        if (currentAdvisees == null) {
            currentAdvisees = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
