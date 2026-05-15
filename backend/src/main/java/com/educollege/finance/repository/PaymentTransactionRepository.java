package com.educollege.finance.repository;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.tuitionFee.student.id = :studentId")
    List<PaymentTransaction> findByStudentId(@Param("studentId") Long studentId);

    List<PaymentTransaction> findByTuitionFeeId(Long tuitionFeeId);

    List<PaymentTransaction> findByStatus(TransactionStatus status);

    List<PaymentTransaction> findByPaymentMethod(String paymentMethod);

    List<PaymentTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<PaymentTransaction> findByReferenceNumber(String referenceNumber);

    long countByStatus(TransactionStatus status);

    long countByTuitionFeeId(Long tuitionFeeId);
}
