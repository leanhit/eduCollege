package com.educollege.finance.repository;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment Transaction Repository
 */
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    
    List<PaymentTransaction> findByTuitionFeeId(Long tuitionFeeId);
    
    List<PaymentTransaction> findByStatus(TransactionStatus status);
    
    List<PaymentTransaction> findByPaymentMethod(String paymentMethod);
    
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.transactionDate BETWEEN :startDate AND :endDate")
    List<PaymentTransaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.referenceNumber = :referenceNumber")
    List<PaymentTransaction> findByReferenceNumber(@Param("referenceNumber") String referenceNumber);
    
    long countByStatus(TransactionStatus status);
    
    long countByTuitionFeeId(Long tuitionFeeId);
}
