package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.Course;
import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course Repository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCode(String code);
    
    List<Course> findByFacultyId(Long facultyId);
    
    List<Course> findByDepartmentId(Long departmentId);
    
    List<Course> findByFaculty(Faculty faculty);
    
    List<Course> findByDepartment(Department department);
    
    List<Course> findByIsActiveTrue();
    
    List<Course> findByIsElectiveTrue();
    
    List<Course> findByIsElectiveFalse();
    
    List<Course> findByFacultyIdAndIsActiveTrue(Long facultyId);
    
    List<Course> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:name% OR c.vietnameseName LIKE %:name% OR c.englishName LIKE %:name%")
    List<Course> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM Course c WHERE c.code LIKE %:code%")
    List<Course> findByCodeContaining(@Param("code") String code);
    
    @Query("SELECT c FROM Course c WHERE c.credits = :credits")
    List<Course> findByCredits(@Param("credits") Integer credits);
    
    @Query("SELECT c FROM Course c WHERE c.credits BETWEEN :minCredits AND :maxCredits")
    List<Course> findByCreditsBetween(@Param("minCredits") Integer minCredits, @Param("maxCredits") Integer maxCredits);
    
    @Query("SELECT c FROM Course c WHERE c.prerequisites LIKE %:prerequisiteCode%")
    List<Course> findByPrerequisiteContaining(@Param("prerequisiteCode") String prerequisiteCode);
    
    @Query("SELECT c FROM Course c WHERE c.faculty.id = :facultyId AND c.isActive = true ORDER BY c.code")
    List<Course> findActiveCoursesByFaculty(@Param("facultyId") Long facultyId);
    
    @Query("SELECT c FROM Course c WHERE c.department.id = :departmentId AND c.isActive = true ORDER BY c.code")
    List<Course> findActiveCoursesByDepartment(@Param("departmentId") Long departmentId);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndFacultyId(String code, Long facultyId);
    
    long countByFacultyId(Long facultyId);
    
    long countByDepartmentId(Long departmentId);
    
    long countByFacultyIdAndIsActiveTrue(Long facultyId);
    
    long countByDepartmentIdAndIsActiveTrue(Long departmentId);
}
