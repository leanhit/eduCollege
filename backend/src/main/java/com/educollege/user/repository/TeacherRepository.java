package com.educollege.user.repository;

import com.educollege.user.model.Teacher;
import com.educollege.academic.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Teacher Repository
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByTeacherNumber(String teacherNumber);
    
    Optional<Teacher> findByUser_Id(Long userId);
    
    List<Teacher> findByDepartmentId(Long departmentId);
    
    List<Teacher> findByDepartment(Department department);
    
    List<Teacher> findByIsActiveTrue();
    
    List<Teacher> findByIsAdvisorTrue();
    
    List<Teacher> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    
    List<Teacher> findByDepartmentIdAndIsAdvisorTrue(Long departmentId);
    
    List<Teacher> findByDepartmentIdAndIsActiveTrueAndIsAdvisorTrue(Long departmentId);
    
    @Query("SELECT t FROM Teacher t WHERE t.academicTitle = :title")
    List<Teacher> findByAcademicTitle(@Param("title") String title);
    
    @Query("SELECT t FROM Teacher t WHERE t.specialization LIKE %:specialization%")
    List<Teacher> findBySpecializationContaining(@Param("specialization") String specialization);
    
    @Query("SELECT t FROM Teacher t WHERE t.email = :email")
    Optional<Teacher> findByEmail(@Param("email") String email);
    
    @Query("SELECT t FROM Teacher t WHERE t.teacherNumber LIKE %:teacherNumber%")
    List<Teacher> findByTeacherNumberContaining(@Param("teacherNumber") String teacherNumber);
    
    @Query("SELECT t FROM Teacher t WHERE t.department.id = :departmentId AND t.isActive = true ORDER BY t.teacherNumber")
    List<Teacher> findActiveTeachersByDepartment(@Param("departmentId") Long departmentId);
    
    @Query("SELECT t FROM Teacher t WHERE t.isAdvisor = true AND t.isActive = true AND t.currentAdvisees < t.maxAdvisees")
    List<Teacher> findAvailableAdvisors();
    
    @Query("SELECT t FROM Teacher t WHERE t.currentCoursesPerSemester < t.maxCoursesPerSemester AND t.isActive = true")
    List<Teacher> findAvailableTeachers();
    
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department.id = :departmentId AND t.isActive = true")
    long countActiveTeachersByDepartment(@Param("departmentId") Long departmentId);
    
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.isAdvisor = true AND t.isActive = true")
    long countActiveAdvisors();
    
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department.id = :departmentId AND t.isAdvisor = true AND t.isActive = true")
    long countActiveAdvisorsByDepartment(@Param("departmentId") Long departmentId);
    
    boolean existsByTeacherNumber(String teacherNumber);
    
    boolean existsByUser_Id(Long userId);
    
    long countByDepartmentId(Long departmentId);
    
    long countByIsActiveTrue();
    
    long countByIsAdvisorTrue();
    
    long countByIsActiveTrueAndIsAdvisorTrue();
    
    @Query("UPDATE Teacher t SET t.currentCoursesPerSemester = t.currentCoursesPerSemester + 1 WHERE t.id = :teacherId")
    @Modifying
    void incrementCurrentCourses(@Param("teacherId") Long teacherId);
    
    @Query("UPDATE Teacher t SET t.currentCoursesPerSemester = CASE WHEN t.currentCoursesPerSemester > 0 THEN t.currentCoursesPerSemester - 1 ELSE 0 END WHERE t.id = :teacherId")
    @Modifying
    void decrementCurrentCourses(@Param("teacherId") Long teacherId);
    
    @Query("UPDATE Teacher t SET t.currentAdvisees = t.currentAdvisees + 1 WHERE t.id = :teacherId")
    @Modifying
    void incrementCurrentAdvisees(@Param("teacherId") Long teacherId);
    
    @Query("UPDATE Teacher t SET t.currentAdvisees = CASE WHEN t.currentAdvisees > 0 THEN t.currentAdvisees - 1 ELSE 0 END WHERE t.id = :teacherId")
    @Modifying
    void decrementCurrentAdvisees(@Param("teacherId") Long teacherId);
}
