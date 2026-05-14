package com.educollege.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payment Transaction Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionRequest {
    
    private Long tuitionFeeId;
    private BigDecimal amount;
    private String paymentMethod;
    private String referenceNumber;
}
