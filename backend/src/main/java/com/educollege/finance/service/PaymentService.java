package com.educollege.finance.service;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.repository.PaymentTransactionRepository;
import com.educollege.finance.repository.TuitionFeeRepository;
import com.educollege.finance.enums.TransactionStatus;
import com.educollege.finance.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment Service for handling student tuition payments
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final TuitionFeeRepository tuitionFeeRepository;

    /**
     * Process a new payment for a specific tuition fee record
     */
    public PaymentTransaction processPayment(Long tuitionFeeId, BigDecimal amount, String paymentMethod, String reference) {
        log.info("Processing payment of {} for tuitionFeeId={}", amount, tuitionFeeId);

        TuitionFee tuitionFee = tuitionFeeRepository.findById(tuitionFeeId)
                .orElseThrow(() -> new RuntimeException("Tuition fee record not found"));

        // Create transaction
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTuitionFee(tuitionFee);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setReferenceNumber(reference);
        transaction.setStatus(TransactionStatus.SUCCESS);

        PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);

        // Update TuitionFee paid amount
        BigDecimal newPaidAmount = tuitionFee.getPaidAmount().add(amount);
        tuitionFee.setPaidAmount(newPaidAmount);

        // Update status if fully paid
        if (newPaidAmount.compareTo(tuitionFee.getTotalAmount()) >= 0) {
            tuitionFee.setStatus(PaymentStatus.PAID);
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            tuitionFee.setStatus(PaymentStatus.PARTIAL);
        }

        tuitionFeeRepository.save(tuitionFee);
        log.info("Payment processed successfully. New balance: {}", tuitionFee.getTotalAmount().subtract(newPaidAmount));

        return savedTransaction;
    }

    @Transactional(readOnly = true)
    public List<PaymentTransaction> getStudentPaymentHistory(Long studentId) {
        return paymentTransactionRepository.findByStudentId(studentId);
    }
}
