package com.educollege.academic.controller;

import com.educollege.academic.model.AdvisingSession;
import com.educollege.academic.service.AdvisingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Advising Controller
 */
@RestController
@RequestMapping("/api/v1/academic/advising")
@RequiredArgsConstructor
public class AdvisingController {
    
    private final AdvisingService advisingService;
    
    @PostMapping
    public ResponseEntity<AdvisingSession> createAdvisingSession(@RequestBody AdvisingSession advisingSession) {
        AdvisingSession createdSession = advisingService.createAdvisingSession(advisingSession);
        return ResponseEntity.ok(createdSession);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AdvisingSession> updateAdvisingSession(@PathVariable Long id, @RequestBody AdvisingSession advisingSession) {
        AdvisingSession updatedSession = advisingService.updateAdvisingSession(id, advisingSession);
        return ResponseEntity.ok(updatedSession);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvisingSession(@PathVariable Long id) {
        advisingService.deleteAdvisingSession(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdvisingSession> getAdvisingSessionById(@PathVariable Long id) {
        return advisingService.getAdvisingSessionById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<AdvisingSession>> getAllAdvisingSessions() {
        List<AdvisingSession> sessions = advisingService.getAllAdvisingSessions();
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByStudentId(@PathVariable Long studentId) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByStudentId(studentId);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/advisor/{advisorId}")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByAdvisorId(@PathVariable Long advisorId) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByAdvisorId(advisorId);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByStudentIdAndStatus(@PathVariable Long studentId, 
                                                                                         @PathVariable AdvisingSession.AdvisingSessionStatus status) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByStudentIdAndStatus(studentId, status);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/advisor/{advisorId}/status/{status}")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByAdvisorIdAndStatus(@PathVariable Long advisorId, 
                                                                                       @PathVariable AdvisingSession.AdvisingSessionStatus status) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByAdvisorIdAndStatus(advisorId, status);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/advisor/{advisorId}/upcoming")
    public ResponseEntity<List<AdvisingSession>> getUpcomingAdvisingSessions(@PathVariable Long advisorId) {
        List<AdvisingSession> sessions = advisingService.getUpcomingAdvisingSessionsForAdvisor(advisorId);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByDateRange(@RequestParam LocalDateTime startDate, 
                                                                                  @RequestParam LocalDateTime endDate) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/student/{studentId}/date-range")
    public ResponseEntity<List<AdvisingSession>> getAdvisingSessionsByStudentIdAndDateRange(@PathVariable Long studentId, 
                                                                                            @RequestParam LocalDateTime startDate, 
                                                                                            @RequestParam LocalDateTime endDate) {
        List<AdvisingSession> sessions = advisingService.getAdvisingSessionsByStudentIdAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/student/{studentId}/count")
    public ResponseEntity<Long> countAdvisingSessionsByStudentId(@PathVariable Long studentId) {
        Long count = advisingService.countAdvisingSessionsByStudentId(studentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/advisor/{advisorId}/count")
    public ResponseEntity<Long> countAdvisingSessionsByAdvisorId(@PathVariable Long advisorId) {
        Long count = advisingService.countAdvisingSessionsByAdvisorId(advisorId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countAdvisingSessionsByStatus(@PathVariable AdvisingSession.AdvisingSessionStatus status) {
        Long count = advisingService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<AdvisingSession> completeAdvisingSession(@PathVariable Long id, @RequestParam String notes) {
        AdvisingSession completedSession = advisingService.completeAdvisingSession(id, notes);
        return ResponseEntity.ok(completedSession);
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AdvisingSession> cancelAdvisingSession(@PathVariable Long id, @RequestParam String reason) {
        AdvisingSession cancelledSession = advisingService.cancelAdvisingSession(id, reason);
        return ResponseEntity.ok(cancelledSession);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<AdvisingSession> approveStudyPlan(@PathVariable Long id, @RequestParam String notes) {
        AdvisingSession approvedSession = advisingService.approveStudyPlan(id, notes);
        return ResponseEntity.ok(approvedSession);
    }
}
