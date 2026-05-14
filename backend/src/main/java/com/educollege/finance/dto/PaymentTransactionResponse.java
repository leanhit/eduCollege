package com.educollege.finance.dto;

import com.educollege.finance.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment Transaction Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionResponse {
    
    private Long id;
    private Long tuitionFeeId;
    private BigDecimal amount;
    private String paymentMethod;
    private String referenceNumber;
    private TransactionStatus status;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
