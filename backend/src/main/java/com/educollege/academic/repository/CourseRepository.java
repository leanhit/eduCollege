package com.educollege.academic.repository;

import com.educollege.academic.model.Course;
import com.educollege.academic.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course Repository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    
    Optional<Course> findByCode(String code);
    
    List<Course> findByDepartmentId(Long departmentId);
    
    List<Course> findByDepartment(Department department);
    
    List<Course> findByIsActiveTrue();
    
    List<Course> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:name% OR c.vietnameseName LIKE %:name% OR c.englishName LIKE %:name%")
    List<Course> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM Course c WHERE c.code LIKE %:code%")
    List<Course> findByCodeContaining(@Param("code") String code);
    
    @Query("SELECT c FROM Course c WHERE c.credits = :credits")
    List<Course> findByCredits(@Param("credits") Integer credits);
    
    @Query("SELECT c FROM Course c WHERE c.credits BETWEEN :minCredits AND :maxCredits")
    List<Course> findByCreditsBetween(@Param("minCredits") Integer minCredits, @Param("maxCredits") Integer maxCredits);
    
    @Query("SELECT c FROM Course c WHERE c.department.id = :departmentId AND c.isActive = true ORDER BY c.code")
    List<Course> findActiveCoursesByDepartment(@Param("departmentId") Long departmentId);
    
    boolean existsByCode(String code);
    
    long countByDepartmentId(Long departmentId);
    
    long countByDepartmentIdAndIsActiveTrue(Long departmentId);
}
