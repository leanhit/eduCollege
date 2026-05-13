package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.Enrollment;
import com.chatbot.core.academic.model.Student;
import com.chatbot.core.academic.model.CourseOffering;
import com.chatbot.core.academic.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Enrollment Repository
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    Optional<Enrollment> findByStudentIdAndCourseOfferingId(Long studentId, Long courseOfferingId);
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findByStudent(Student student);
    
    List<Enrollment> findByCourseOfferingId(Long courseOfferingId);
    
    List<Enrollment> findByCourseOffering(CourseOffering courseOffering);
    
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
    
    List<Enrollment> findByStudentIdAndCourseOfferingIdAndStatus(Long studentId, Long courseOfferingId, EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId")
    List<Enrollment> findByStudentIdAndSemesterId(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.courseOffering.id = :courseOfferingId AND e.status = :status")
    List<Enrollment> findByCourseOfferingIdAndStatus(@Param("courseOfferingId") Long courseOfferingId, @Param("status") EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId AND e.status = :status")
    List<Enrollment> findByStudentIdAndSemesterIdAndStatus(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId, @Param("status") EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.course.id = :courseId")
    List<Enrollment> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.course.id = :courseId AND e.status = :status")
    List<Enrollment> findByStudentIdAndCourseIdAndStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("status") EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.grade IS NOT NULL AND e.grade >= :minGrade AND e.grade <= :maxGrade")
    List<Enrollment> findByGradeRange(@Param("minGrade") Double minGrade, @Param("maxGrade") Double maxGrade);
    
    @Query("SELECT e FROM Enrollment e WHERE e.letterGrade = :letterGrade")
    List<Enrollment> findByLetterGrade(@Param("letterGrade") String letterGrade);
    
    @Query("SELECT e FROM Enrollment e WHERE e.attendanceRate >= :minAttendance AND e.attendanceRate <= :maxAttendance")
    List<Enrollment> findByAttendanceRange(@Param("minAttendance") Double minAttendance, @Param("maxAttendance") Double maxAttendance);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId")
    long countByStudentIdAndSemesterId(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseOffering.id = :courseOfferingId AND e.status = :status")
    long countByCourseOfferingIdAndStatus(@Param("courseOfferingId") Long courseOfferingId, @Param("status") EnrollmentStatus status);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId AND e.status = :status")
    long countByStudentIdAndSemesterIdAndStatus(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId, @Param("status") EnrollmentStatus status);
    
    @Query("SELECT AVG(e.grade) FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId AND e.grade IS NOT NULL")
    Double getAverageGradeByStudentAndSemester(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT AVG(e.grade) FROM Enrollment e WHERE e.courseOffering.id = :courseOfferingId AND e.grade IS NOT NULL")
    Double getAverageGradeByCourseOffering(@Param("courseId") Long courseOfferingId);
    
    @Query("SELECT SUM(e.courseOffering.course.credits) FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId AND e.status = 'COMPLETED'")
    Integer getCompletedCreditsByStudentAndSemester(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseOffering.semester.id = :semesterId ORDER BY e.courseOffering.course.code")
    List<Enrollment> findByStudentIdAndSemesterIdOrderByCourse(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.courseOffering.id = :courseOfferingId ORDER BY e.student.studentNumber")
    List<Enrollment> findByCourseOfferingIdOrderByStudent(@Param("courseOfferingId") Long courseOfferingId);
    
    boolean existsByStudentIdAndCourseOfferingId(Long studentId, Long courseOfferingId);
    
    long countByStudentId(Long studentId);
    
    long countByCourseOfferingId(Long courseOfferingId);
    
    long countByStatus(EnrollmentStatus status);
    
    long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
}
