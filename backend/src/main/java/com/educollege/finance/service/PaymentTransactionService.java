package com.educollege.finance.service;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.repository.PaymentTransactionRepository;
import com.educollege.finance.repository.TuitionFeeRepository;
import com.educollege.finance.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Payment Transaction Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentTransactionService {
    
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final TuitionFeeRepository tuitionFeeRepository;
    private final TuitionFeeService tuitionFeeService;
    
    public PaymentTransaction createPaymentTransaction(PaymentTransaction paymentTransaction) {
        System.out.println("Creating payment transaction");
        
        TuitionFee tuitionFee = tuitionFeeRepository.findById(paymentTransaction.getTuitionFee().getId())
            .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + paymentTransaction.getTuitionFee().getId()));
        
        paymentTransaction.setTuitionFee(tuitionFee);
        paymentTransaction.setTransactionDate(LocalDateTime.now());
        paymentTransaction.setStatus(TransactionStatus.PENDING);
        
        PaymentTransaction savedTransaction = paymentTransactionRepository.save(paymentTransaction);
        System.out.println("Payment transaction created successfully");
        return savedTransaction;
    }
    
    public PaymentTransaction processPaymentTransaction(Long id, TransactionStatus status) {
        System.out.println("Processing payment transaction with id: " + id + ", status: " + status);
        
        PaymentTransaction transaction = paymentTransactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment transaction not found with id: " + id));
        
        transaction.setStatus(status);
        
        if (status == TransactionStatus.SUCCESS) {
            // Update tuition fee with payment
            tuitionFeeService.recordPayment(transaction.getTuitionFee().getId(), transaction.getAmount());
        }
        
        PaymentTransaction processedTransaction = paymentTransactionRepository.save(transaction);
        System.out.println("Payment transaction processed successfully");
        return processedTransaction;
    }
    
    public PaymentTransaction updatePaymentTransaction(Long id, PaymentTransaction paymentTransaction) {
        System.out.println("Updating payment transaction with id: " + id);
        
        PaymentTransaction existingTransaction = paymentTransactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment transaction not found with id: " + id));
        
        if (paymentTransaction.getTuitionFee() != null && 
            !existingTransaction.getTuitionFee().getId().equals(paymentTransaction.getTuitionFee().getId())) {
            TuitionFee tuitionFee = tuitionFeeRepository.findById(paymentTransaction.getTuitionFee().getId())
                .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + paymentTransaction.getTuitionFee().getId()));
            existingTransaction.setTuitionFee(tuitionFee);
        }
        
        existingTransaction.setAmount(paymentTransaction.getAmount());
        existingTransaction.setPaymentMethod(paymentTransaction.getPaymentMethod());
        existingTransaction.setReferenceNumber(paymentTransaction.getReferenceNumber());
        
        PaymentTransaction updatedTransaction = paymentTransactionRepository.save(existingTransaction);
        System.out.println("Payment transaction updated successfully");
        return updatedTransaction;
    }
    
    public void deletePaymentTransaction(Long id) {
        System.out.println("Deleting payment transaction with id: " + id);
        
        PaymentTransaction transaction = paymentTransactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment transaction not found with id: " + id));
        
        paymentTransactionRepository.delete(transaction);
        System.out.println("Payment transaction deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Optional<PaymentTransaction> getPaymentTransactionById(Long id) {
        return paymentTransactionRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getAllPaymentTransactions() {
        return paymentTransactionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentTransactionsByTuitionFeeId(Long tuitionFeeId) {
        return paymentTransactionRepository.findByTuitionFeeId(tuitionFeeId);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentTransactionsByStatus(TransactionStatus status) {
        return paymentTransactionRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentTransactionsByPaymentMethod(String paymentMethod) {
        return paymentTransactionRepository.findByPaymentMethod(paymentMethod);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentTransactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentTransaction> getPaymentTransactionsByReferenceNumber(String referenceNumber) {
        return paymentTransactionRepository.findByReferenceNumber(referenceNumber);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByTuitionFeeId(Long tuitionFeeId) {
        List<PaymentTransaction> transactions = paymentTransactionRepository.findByTuitionFeeId(tuitionFeeId);
        return transactions.stream()
            .filter(t -> t.getStatus() == TransactionStatus.SUCCESS)
            .map(PaymentTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByStatus(TransactionStatus status) {
        List<PaymentTransaction> transactions = paymentTransactionRepository.findByStatus(status);
        return transactions.stream()
            .map(PaymentTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public long countPaymentTransactionsByStatus(TransactionStatus status) {
        return paymentTransactionRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public long countPaymentTransactionsByTuitionFeeId(Long tuitionFeeId) {
        return paymentTransactionRepository.countByTuitionFeeId(tuitionFeeId);
    }
}
