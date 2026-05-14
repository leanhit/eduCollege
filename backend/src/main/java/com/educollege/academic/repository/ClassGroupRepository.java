package com.educollege.academic.repository;

import com.educollege.academic.model.ClassGroup;
import com.educollege.academic.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Class Group Repository
 */
@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
    
    Optional<ClassGroup> findByCode(String code);
    
    List<ClassGroup> findByFacultyId(Long facultyId);
    
    List<ClassGroup> findByFaculty(Faculty faculty);
    
    List<ClassGroup> findByEnrollmentYear(Integer enrollmentYear);
    
    List<ClassGroup> findByFacultyIdAndEnrollmentYear(Long facultyId, Integer enrollmentYear);
    
    List<ClassGroup> findByIsActiveTrue();
    
    List<ClassGroup> findByFacultyIdAndIsActiveTrue(Long facultyId);
    
    List<ClassGroup> findByEnrollmentYearAndIsActiveTrue(Integer enrollmentYear);
    
    @Query("SELECT c FROM ClassGroup c WHERE c.name LIKE %:name%")
    List<ClassGroup> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM ClassGroup c WHERE c.code LIKE %:code%")
    List<ClassGroup> findByCodeContaining(@Param("code") String code);
    
    @Query("SELECT c FROM ClassGroup c WHERE c.faculty.id = :facultyId AND c.enrollmentYear = :year AND c.isActive = true")
    List<ClassGroup> findActiveClassGroupsByFacultyAndYear(@Param("facultyId") Long facultyId, @Param("year") Integer year);
    
    @Query("SELECT c FROM ClassGroup c WHERE c.currentStudents < c.maxStudents AND c.isActive = true")
    List<ClassGroup> findAvailableClassGroups();
    
    @Query("SELECT c FROM ClassGroup c WHERE c.faculty.id = :facultyId AND c.currentStudents < c.maxStudents AND c.isActive = true")
    List<ClassGroup> findAvailableClassGroupsByFaculty(@Param("facultyId") Long facultyId);
    
    @Query("SELECT AVG(c.currentStudents) FROM ClassGroup c WHERE c.faculty.id = :facultyId AND c.isActive = true")
    Double getAverageStudentsPerClassByFaculty(@Param("facultyId") Long facultyId);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndFacultyId(String code, Long facultyId);
    
    long countByFacultyId(Long facultyId);
    
    long countByEnrollmentYear(Integer enrollmentYear);
    
    long countByFacultyIdAndEnrollmentYear(Long facultyId, Integer enrollmentYear);
    
    long countByFacultyIdAndIsActiveTrue(Long facultyId);
    
    long countByIsActiveTrue();
}
