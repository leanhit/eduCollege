package com.educollege.academic.repository;

import com.educollege.academic.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    
    Optional<Major> findByCode(String code);
    
    List<Major> findByFacultyId(Long facultyId);
    
    List<Major> findByFacultyIdAndIsActiveTrue(Long facultyId);
    
    List<Major> findByIsActiveTrue();
    
    boolean existsByCode(String code);
}
