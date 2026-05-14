package com.educollege.finance.repository;

import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Tuition Fee Repository
 */
@Repository
public interface TuitionFeeRepository extends JpaRepository<TuitionFee, Long> {
    
    Optional<TuitionFee> findByStudentIdAndSemesterId(Long studentId, Long semesterId);
    
    List<TuitionFee> findByStudentId(Long studentId);
    
    List<TuitionFee> findBySemesterId(Long semesterId);
    
    List<TuitionFee> findByStatus(PaymentStatus status);
    
    @Query("SELECT tf FROM TuitionFee tf WHERE tf.dueDate < :date AND tf.status = :status")
    List<TuitionFee> findByDueDateBeforeAndStatus(@Param("date") LocalDate date, @Param("status") PaymentStatus status);
    
    @Query("SELECT tf FROM TuitionFee tf WHERE tf.dueDate BETWEEN :startDate AND :endDate")
    List<TuitionFee> findByDueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    boolean existsByStudentIdAndSemesterId(Long studentId, Long semesterId);
    
    long countByStatus(PaymentStatus status);
}
