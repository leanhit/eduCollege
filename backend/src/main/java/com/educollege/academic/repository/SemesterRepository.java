package com.educollege.academic.repository;

import com.educollege.academic.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Semester Repository
 */
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    
    Optional<Semester> findByCode(String code);
    
    List<Semester> findByAcademicYear(String academicYear);
    
    List<Semester> findByAcademicYearOrderBySemesterNumber(String academicYear);
    
    List<Semester> findByIsActiveTrue();
    
    List<Semester> findByIsActiveFalse();
    
    @Query("SELECT s FROM Semester s WHERE s.semesterNumber = :semesterNumber AND s.academicYear = :academicYear")
    Optional<Semester> findBySemesterNumberAndAcademicYear(@Param("semesterNumber") Integer semesterNumber, @Param("academicYear") String academicYear);
    
    @Query("SELECT s FROM Semester s WHERE s.startDate <= :date AND s.endDate >= :date")
    Optional<Semester> findCurrentSemester(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM Semester s WHERE s.registrationStart <= :date AND s.registrationEnd >= :date")
    Optional<Semester> findRegistrationOpenSemester(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM Semester s WHERE s.addDropStart <= :date AND s.addDropEnd >= :date")
    Optional<Semester> findAddDropOpenSemester(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM Semester s WHERE s.examStart <= :date AND s.examEnd >= :date")
    Optional<Semester> findExamPeriodSemester(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM Semester s WHERE s.academicYear LIKE %:year%")
    List<Semester> findByAcademicYearContaining(@Param("year") String year);
    
    @Query("SELECT s FROM Semester s WHERE s.name LIKE %:name%")
    List<Semester> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT s FROM Semester s WHERE s.isActive = true ORDER BY s.academicYear DESC, s.semesterNumber DESC")
    List<Semester> findActiveSemestersOrderByDate();
    
    Optional<Semester> findTopByOrderByAcademicYearDescSemesterNumberDesc();
    
    boolean existsByCode(String code);
    
    boolean existsByAcademicYearAndSemesterNumber(String academicYear, Integer semesterNumber);
    
    long countByAcademicYear(String academicYear);
    
    long countByIsActiveTrue();
    
    long countByIsActiveFalse();
}
