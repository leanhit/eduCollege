package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.Student;
import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.model.ClassGroup;
import com.chatbot.core.academic.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Student Repository
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentNumber(String studentNumber);
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByFacultyId(Long facultyId);
    
    List<Student> findByFaculty(Faculty faculty);
    
    List<Student> findByDepartmentId(Long departmentId);
    
    List<Student> findByClassGroupId(Long classGroupId);
    
    List<Student> findByClassGroup(ClassGroup classGroup);
    
    List<Student> findByEnrollmentYear(Integer enrollmentYear);
    
    List<Student> findByGraduationYear(Integer graduationYear);
    
    List<Student> findByStudentStatus(StudentStatus studentStatus);
    
    List<Student> findByAcademicStanding(String academicStanding);
    
    List<Student> findByAdvisorId(Long advisorId);
    
    List<Student> findByIsActiveTrue();
    
    List<Student> findByFacultyIdAndIsActiveTrue(Long facultyId);
    
    List<Student> findByClassGroupIdAndIsActiveTrue(Long classGroupId);
    
    List<Student> findByEnrollmentYearAndIsActiveTrue(Integer enrollmentYear);
    
    List<Student> findByStudentStatusAndIsActiveTrue(StudentStatus studentStatus);
    
    @Query("SELECT s FROM Student s WHERE s.studentNumber LIKE %:studentNumber%")
    List<Student> findByStudentNumberContaining(@Param("studentNumber") String studentNumber);
    
    @Query("SELECT s FROM Student s WHERE s.faculty.id = :facultyId AND s.enrollmentYear = :year AND s.isActive = true")
    List<Student> findActiveStudentsByFacultyAndYear(@Param("facultyId") Long facultyId, @Param("year") Integer year);
    
    @Query("SELECT s FROM Student s WHERE s.classGroup.id = :classGroupId AND s.isActive = true ORDER BY s.studentNumber")
    List<Student> findActiveStudentsByClassGroup(@Param("classGroupId") Long classGroupId);
    
    @Query("SELECT s FROM Student s WHERE s.advisorId = :advisorId AND s.isActive = true")
    List<Student> findActiveAdvisees(@Param("advisorId") Long advisorId);
    
    @Query("SELECT s FROM Student s WHERE s.currentGpa >= :minGpa AND s.currentGpa <= :maxGpa")
    List<Student> findByGpaRange(@Param("minGpa") Double minGpa, @Param("maxGpa") Double maxGpa);
    
    @Query("SELECT s FROM Student s WHERE s.academicStanding = :standing AND s.isActive = true")
    List<Student> findActiveStudentsByAcademicStanding(@Param("standing") String standing);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.faculty.id = :facultyId AND s.isActive = true")
    long countActiveStudentsByFaculty(@Param("facultyId") Long facultyId);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.classGroup.id = :classGroupId AND s.isActive = true")
    long countActiveStudentsByClassGroup(@Param("classGroupId") Long classGroupId);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.advisorId = :advisorId AND s.isActive = true")
    long countActiveAdvisees(@Param("advisorId") Long advisorId);
    
    @Query("SELECT AVG(s.currentGpa) FROM Student s WHERE s.faculty.id = :facultyId AND s.isActive = true")
    Double getAverageGpaByFaculty(@Param("facultyId") Long facultyId);
    
    @Query("SELECT s FROM Student s WHERE s.studentStatus = :status AND s.isActive = true ORDER BY s.studentNumber")
    List<Student> findActiveStudentsByStatus(@Param("status") StudentStatus status);
    
    boolean existsByStudentNumber(String studentNumber);
    
    boolean existsByUserId(Long userId);
    
    long countByFacultyId(Long facultyId);
    
    long countByClassGroupId(Long classGroupId);
    
    long countByEnrollmentYear(Integer enrollmentYear);
    
    long countByGraduationYear(Integer graduationYear);
    
    long countByStudentStatus(StudentStatus studentStatus);
    
    long countByAcademicStanding(String academicStanding);
    
    long countByAdvisorId(Long advisorId);
    
    long countByIsActiveTrue();
}
