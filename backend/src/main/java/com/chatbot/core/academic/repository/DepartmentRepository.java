package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.Department;
import com.chatbot.core.academic.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Department Repository
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByCode(String code);
    
    List<Department> findByFacultyId(Long facultyId);
    
    List<Department> findByFaculty(Faculty faculty);
    
    List<Department> findByIsActiveTrue();
    
    List<Department> findByFacultyIdAndIsActiveTrue(Long facultyId);
    
    @Query("SELECT d FROM Department d WHERE d.name LIKE %:name% OR d.vietnameseName LIKE %:name%")
    List<Department> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT d FROM Department d WHERE d.code LIKE %:code%")
    List<Department> findByCodeContaining(@Param("code") String code);
    
    @Query("SELECT d FROM Department d WHERE d.faculty.id = :facultyId AND d.isActive = true")
    List<Department> findActiveDepartmentsByFaculty(@Param("facultyId") Long facultyId);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndFacultyId(String code, Long facultyId);
    
    long countByFacultyId(Long facultyId);
    
    long countByFacultyIdAndIsActiveTrue(Long facultyId);
}
