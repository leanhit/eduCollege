package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.AdvisingSession;
import com.chatbot.core.academic.model.Student;
import com.chatbot.core.academic.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Advising Session Repository
 */
@Repository
public interface AdvisingSessionRepository extends JpaRepository<AdvisingSession, Long> {
    
    List<AdvisingSession> findByStudentId(Long studentId);
    
    List<AdvisingSession> findByStudent(Student student);
    
    List<AdvisingSession> findByAdvisorId(Long advisorId);
    
    List<AdvisingSession> findByAdvisor(Teacher advisor);
    
    List<AdvisingSession> findByStatus(AdvisingSession.AdvisingSessionStatus status);
    
    List<AdvisingSession> findBySessionType(String sessionType);
    
    List<AdvisingSession> findByStudentIdAndAdvisorId(Long studentId, Long advisorId);
    
    List<AdvisingSession> findByStudentIdAndStatus(Long studentId, AdvisingSession.AdvisingSessionStatus status);
    
    List<AdvisingSession> findByAdvisorIdAndStatus(Long advisorId, AdvisingSession.AdvisingSessionStatus status);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.sessionDate BETWEEN :startDate AND :endDate")
    List<AdvisingSession> findBySessionDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.student.id = :studentId AND a.sessionDate BETWEEN :startDate AND :endDate")
    List<AdvisingSession> findByStudentIdAndSessionDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.sessionDate BETWEEN :startDate AND :endDate")
    List<AdvisingSession> findByAdvisorIdAndSessionDateBetween(@Param("advisorId") Long advisorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.sessionDate >= :date AND a.status = :status")
    List<AdvisingSession> findBySessionDateAfterAndStatus(@Param("date") LocalDateTime date, @Param("status") AdvisingSession.AdvisingSessionStatus status);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.followUpRequired = true AND a.followUpDate <= :date")
    List<AdvisingSession> findPendingFollowUps(@Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.student.id = :studentId AND a.followUpRequired = true")
    List<AdvisingSession> findFollowUpRequiredByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.followUpRequired = true")
    List<AdvisingSession> findFollowUpRequiredByAdvisor(@Param("advisorId") Long advisorId);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.studentRating IS NOT NULL")
    List<AdvisingSession> findSessionsWithStudentRating();
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.advisorRating IS NOT NULL")
    List<AdvisingSession> findSessionsWithAdvisorRating();
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.location = :location")
    List<AdvisingSession> findByLocation(@Param("location") String location);
    
    @Query("SELECT COUNT(a) FROM AdvisingSession a WHERE a.student.id = :studentId AND a.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") AdvisingSession.AdvisingSessionStatus status);
    
    @Query("SELECT COUNT(a) FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.status = :status")
    long countByAdvisorIdAndStatus(@Param("advisorId") Long advisorId, @Param("status") AdvisingSession.AdvisingSessionStatus status);
    
    @Query("SELECT COUNT(a) FROM AdvisingSession a WHERE a.student.id = :studentId AND a.sessionDate BETWEEN :startDate AND :endDate")
    long countByStudentIdAndSessionDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(a) FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.sessionDate BETWEEN :startDate AND :endDate")
    long countByAdvisorIdAndSessionDateBetween(@Param("advisorId") Long advisorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(a.studentRating) FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.studentRating IS NOT NULL")
    Double getAverageStudentRatingByAdvisor(@Param("advisorId") Long advisorId);
    
    @Query("SELECT AVG(a.advisorRating) FROM AdvisingSession a WHERE a.advisor.id = :advisorId AND a.advisorRating IS NOT NULL")
    Double getAverageAdvisorRatingByAdvisor(@Param("advisorId") Long advisorId);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.student.id = :studentId ORDER BY a.sessionDate DESC")
    List<AdvisingSession> findByStudentIdOrderBySessionDateDesc(@Param("studentId") Long studentId);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.advisor.id = :advisorId ORDER BY a.sessionDate DESC")
    List<AdvisingSession> findByAdvisorIdOrderBySessionDateDesc(@Param("advisorId") Long advisorId);
    
    @Query("SELECT a FROM AdvisingSession a WHERE a.sessionDate >= :now AND a.status = 'SCHEDULED' ORDER BY a.sessionDate ASC")
    List<AdvisingSession> findUpcomingSessions(@Param("now") LocalDateTime now);
    
    long countByStudentId(Long studentId);
    
    long countByAdvisorId(Long advisorId);
    
    long countByStatus(AdvisingSession.AdvisingSessionStatus status);
    
    long countBySessionType(String sessionType);
    
    long countByFollowUpRequiredTrue();
}
