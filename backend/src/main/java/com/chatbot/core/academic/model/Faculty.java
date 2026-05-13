package com.chatbot.core.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Faculty Entity
 */
@Entity
@Table(name = "faculties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 10)
    private String code; // "CNTT", "TOAN", "LY"
    
    @Column(nullable = false, length = 200)
    private String name; // "Khoa Công nghệ Thông tin"
    
    @Column(name = "vietnamese_name", nullable = false, length = 200)
    private String vietnameseName; // "Khoa Công nghệ Thông tin"
    
    @Column(name = "english_name", length = 200)
    private String englishName; // "Faculty of Information Technology"
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;
    
    @Column(name = "contact_email", length = 100)
    private String contactEmail;
    
    @Column(name = "office_location", length = 200)
    private String officeLocation;
    
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
