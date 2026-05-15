package com.educollege.academic.repository;

import com.educollege.academic.model.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, Long> {
    
    Optional<Sequence> findBySequenceKey(String sequenceKey);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Sequence> findBySequenceKeyAndIsActiveTrue(String sequenceKey);
    
    boolean existsBySequenceKey(String sequenceKey);
}
