package com.educollege.academic.service;

import com.educollege.academic.model.AdvisingSession;
import com.educollege.academic.repository.AdvisingSessionRepository;
import com.educollege.user.model.Student;
import com.educollege.user.model.Teacher;
import com.educollege.user.repository.StudentRepository;
import com.educollege.user.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Advising Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdvisingService {
    
    private final AdvisingSessionRepository advisingSessionRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    public AdvisingSession createAdvisingSession(AdvisingSession advisingSession) {
        System.out.println("Creating advising session");
        
        Student student = studentRepository.findById(advisingSession.getStudent().getId())
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + advisingSession.getStudent().getId()));
        
        Teacher advisor = teacherRepository.findById(advisingSession.getAdvisor().getId())
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + advisingSession.getAdvisor().getId()));
        
        if (!advisor.getIsAdvisor()) {
            throw new RuntimeException("Teacher is not an advisor");
        }
        
        advisingSession.setStudent(student);
        advisingSession.setAdvisor(advisor);
        
        AdvisingSession savedSession = advisingSessionRepository.save(advisingSession);
        System.out.println("Advising session created successfully");
        return savedSession;
    }
    
    public AdvisingSession updateAdvisingSession(Long id, AdvisingSession advisingSession) {
        System.out.println("Updating advising session with id: " + id);
        
        AdvisingSession existingSession = advisingSessionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Advising session not found with id: " + id));
        
        if (advisingSession.getStudent() != null && 
            !existingSession.getStudent().getId().equals(advisingSession.getStudent().getId())) {
            Student student = studentRepository.findById(advisingSession.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + advisingSession.getStudent().getId()));
            existingSession.setStudent(student);
        }
        
        if (advisingSession.getAdvisor() != null && 
            !existingSession.getAdvisor().getId().equals(advisingSession.getAdvisor().getId())) {
            Teacher advisor = teacherRepository.findById(advisingSession.getAdvisor().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + advisingSession.getAdvisor().getId()));
            if (!advisor.getIsAdvisor()) {
                throw new RuntimeException("Teacher is not an advisor");
            }
            existingSession.setAdvisor(advisor);
        }
        
        existingSession.setSessionDate(advisingSession.getSessionDate());
        existingSession.setSessionType(advisingSession.getSessionType());
        existingSession.setSessionTitle(advisingSession.getSessionTitle());
        existingSession.setNotes(advisingSession.getNotes());
        existingSession.setStatus(advisingSession.getStatus());
        existingSession.setFollowUpRequired(advisingSession.getFollowUpRequired());
        existingSession.setFollowUpDate(advisingSession.getFollowUpDate());
        
        AdvisingSession updatedSession = advisingSessionRepository.save(existingSession);
        System.out.println("Advising session updated successfully");
        return updatedSession;
    }
    
    public void deleteAdvisingSession(Long id) {
        System.out.println("Deleting advising session with id: " + id);
        
        AdvisingSession session = advisingSessionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Advising session not found with id: " + id));
        
        advisingSessionRepository.delete(session);
        System.out.println("Advising session deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Optional<AdvisingSession> getAdvisingSessionById(Long id) {
        return advisingSessionRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAllAdvisingSessions() {
        return advisingSessionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByStudentId(Long studentId) {
        return advisingSessionRepository.findByStudentId(studentId);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByAdvisorId(Long advisorId) {
        return advisingSessionRepository.findByAdvisorId(advisorId);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByStudentIdAndStatus(Long studentId, AdvisingSession.AdvisingSessionStatus status) {
        return advisingSessionRepository.findByStudentIdAndStatus(studentId, status);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByAdvisorIdAndStatus(Long advisorId, AdvisingSession.AdvisingSessionStatus status) {
        return advisingSessionRepository.findByAdvisorIdAndStatus(advisorId, status);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getUpcomingAdvisingSessionsForAdvisor(Long advisorId) {
        LocalDateTime now = LocalDateTime.now();
        return advisingSessionRepository.findBySessionDateAfterAndStatus(now, AdvisingSession.AdvisingSessionStatus.SCHEDULED)
            .stream()
            .filter(session -> session.getAdvisor().getId().equals(advisorId))
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return advisingSessionRepository.findBySessionDateBetween(startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public List<AdvisingSession> getAdvisingSessionsByStudentIdAndDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return advisingSessionRepository.findByStudentIdAndSessionDateBetween(studentId, startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public long countAdvisingSessionsByStudentId(Long studentId) {
        return advisingSessionRepository.countByStudentId(studentId);
    }
    
    @Transactional(readOnly = true)
    public long countAdvisingSessionsByAdvisorId(Long advisorId) {
        return advisingSessionRepository.countByAdvisorId(advisorId);
    }
    
    @Transactional(readOnly = true)
    public long countByStatus(AdvisingSession.AdvisingSessionStatus status) {
        return advisingSessionRepository.countByStatus(status);
    }
    
    public AdvisingSession completeAdvisingSession(Long id, String notes) {
        System.out.println("Completing advising session with id: " + id);
        
        AdvisingSession session = advisingSessionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Advising session not found with id: " + id));
        
        session.setStatus(AdvisingSession.AdvisingSessionStatus.COMPLETED);
        session.setNotes(notes);
        
        AdvisingSession completedSession = advisingSessionRepository.save(session);
        System.out.println("Advising session completed successfully");
        return completedSession;
    }
    
    public AdvisingSession cancelAdvisingSession(Long id, String reason) {
        System.out.println("Cancelling advising session with id: " + id);
        
        AdvisingSession session = advisingSessionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Advising session not found with id: " + id));
        
        session.setStatus(AdvisingSession.AdvisingSessionStatus.CANCELLED);
        session.setNotes(reason);
        
        AdvisingSession cancelledSession = advisingSessionRepository.save(session);
        System.out.println("Advising session cancelled successfully");
        return cancelledSession;
    }
}
