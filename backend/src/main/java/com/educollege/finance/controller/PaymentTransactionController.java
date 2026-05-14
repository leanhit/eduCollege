package com.educollege.finance.controller;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.service.PaymentTransactionService;
import com.educollege.finance.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment Transaction Controller
 */
@RestController
@RequestMapping("/api/v1/finance/payment-transactions")
@RequiredArgsConstructor
public class PaymentTransactionController {
    
    private final PaymentTransactionService paymentTransactionService;
    
    @PostMapping
    public ResponseEntity<PaymentTransaction> createPaymentTransaction(@RequestBody PaymentTransaction paymentTransaction) {
        PaymentTransaction createdTransaction = paymentTransactionService.createPaymentTransaction(paymentTransaction);
        return ResponseEntity.ok(createdTransaction);
    }
    
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentTransaction> processPaymentTransaction(@PathVariable Long id, 
                                                                         @RequestParam TransactionStatus status) {
        PaymentTransaction processedTransaction = paymentTransactionService.processPaymentTransaction(id, status);
        return ResponseEntity.ok(processedTransaction);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTransaction> updatePaymentTransaction(@PathVariable Long id, 
                                                                       @RequestBody PaymentTransaction paymentTransaction) {
        PaymentTransaction updatedTransaction = paymentTransactionService.updatePaymentTransaction(id, paymentTransaction);
        return ResponseEntity.ok(updatedTransaction);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentTransaction(@PathVariable Long id) {
        paymentTransactionService.deletePaymentTransaction(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransaction> getPaymentTransactionById(@PathVariable Long id) {
        return paymentTransactionService.getPaymentTransactionById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> getAllPaymentTransactions() {
        List<PaymentTransaction> transactions = paymentTransactionService.getAllPaymentTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/tuition-fee/{tuitionFeeId}")
    public ResponseEntity<List<PaymentTransaction>> getPaymentTransactionsByTuitionFeeId(@PathVariable Long tuitionFeeId) {
        List<PaymentTransaction> transactions = paymentTransactionService.getPaymentTransactionsByTuitionFeeId(tuitionFeeId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentTransaction>> getPaymentTransactionsByStatus(@PathVariable TransactionStatus status) {
        List<PaymentTransaction> transactions = paymentTransactionService.getPaymentTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<PaymentTransaction>> getPaymentTransactionsByPaymentMethod(@PathVariable String paymentMethod) {
        List<PaymentTransaction> transactions = paymentTransactionService.getPaymentTransactionsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentTransaction>> getPaymentTransactionsByDateRange(@RequestParam LocalDateTime startDate, 
                                                                                      @RequestParam LocalDateTime endDate) {
        List<PaymentTransaction> transactions = paymentTransactionService.getPaymentTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<List<PaymentTransaction>> getPaymentTransactionsByReferenceNumber(@PathVariable String referenceNumber) {
        List<PaymentTransaction> transactions = paymentTransactionService.getPaymentTransactionsByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/tuition-fee/{tuitionFeeId}/total")
    public ResponseEntity<BigDecimal> getTotalAmountByTuitionFeeId(@PathVariable Long tuitionFeeId) {
        BigDecimal total = paymentTransactionService.getTotalAmountByTuitionFeeId(tuitionFeeId);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/status/{status}/total")
    public ResponseEntity<BigDecimal> getTotalAmountByStatus(@PathVariable TransactionStatus status) {
        BigDecimal total = paymentTransactionService.getTotalAmountByStatus(status);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countPaymentTransactionsByStatus(@PathVariable TransactionStatus status) {
        Long count = paymentTransactionService.countPaymentTransactionsByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/tuition-fee/{tuitionFeeId}/count")
    public ResponseEntity<Long> countPaymentTransactionsByTuitionFeeId(@PathVariable Long tuitionFeeId) {
        Long count = paymentTransactionService.countPaymentTransactionsByTuitionFeeId(tuitionFeeId);
        return ResponseEntity.ok(count);
    }
}
