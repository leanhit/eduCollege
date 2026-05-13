package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Faculty Repository
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    Optional<Faculty> findByCode(String code);
    
    List<Faculty> findByIsActiveTrue();
    
    List<Faculty> findByIsActiveFalse();
    
    @Query("SELECT f FROM Faculty f WHERE f.name LIKE %:name% OR f.vietnameseName LIKE %:name%")
    List<Faculty> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT f FROM Faculty f WHERE f.code LIKE %:code%")
    List<Faculty> findByCodeContaining(@Param("code") String code);
    
    boolean existsByCode(String code);
    
    long countByIsActiveTrue();
    
    long countByIsActiveFalse();
}
