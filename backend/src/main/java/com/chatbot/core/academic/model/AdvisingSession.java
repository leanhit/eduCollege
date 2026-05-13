package com.chatbot.core.academic.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Advising Session Entity
 */
@Entity
@Table(name = "advising_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisingSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advisor_id", nullable = false)
    private Teacher advisor;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;
    
    @Column(name = "session_type", nullable = false, length = 50)
    private String sessionType; // ACADEMIC, CAREER, PERSONAL, REGISTRATION
    
    @Column(name = "session_title", length = 200)
    private String sessionTitle;
    
    @Column(name = "session_description", columnDefinition = "TEXT")
    private String sessionDescription;
    
    @Column(name = "location", length = 200)
    private String location;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdvisingSessionStatus status;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;
    
    @Column(name = "action_items", columnDefinition = "TEXT")
    private String actionItems;
    
    @Column(name = "follow_up_required", nullable = false)
    private Boolean followUpRequired;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;
    
    @Column(name = "follow_up_notes", columnDefinition = "TEXT")
    private String followUpNotes;
    
    @Column(name = "student_rating")
    private Integer studentRating; // 1-5 rating
    
    @Column(name = "advisor_rating")
    private Integer advisorRating; // 1-5 rating
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
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
            status = AdvisingSessionStatus.SCHEDULED;
        }
        if (followUpRequired == null) {
            followUpRequired = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Advising Session Status Enumeration
     */
    public enum AdvisingSessionStatus {
        SCHEDULED("Đã lên lịch"),
        COMPLETED("Đã hoàn thành"),
        CANCELLED("Đã hủy"),
        NO_SHOW("Không đến"),
        RESCHEDULED("Đã dời lịch");
        
        private final String displayName;
        
        AdvisingSessionStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
