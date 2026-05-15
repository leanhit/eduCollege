package com.educollege.finance.controller;

import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentTransaction> processPayment(
            @RequestParam Long tuitionFeeId,
            @RequestParam BigDecimal amount,
            @RequestParam String method,
            @RequestParam(required = false) String reference) {
        
        PaymentTransaction transaction = paymentService.processPayment(tuitionFeeId, amount, method, reference);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history/student/{studentId}")
    public ResponseEntity<List<PaymentTransaction>> getHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getStudentPaymentHistory(studentId));
    }
}
