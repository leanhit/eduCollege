package com.educollege.finance.dto;

import com.educollege.finance.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Tuition Fee Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuitionFeeResponse {
    
    private Long id;
    private Long studentId;
    private String studentNumber;
    private String studentName;
    private Long semesterId;
    private String semesterCode;
    private String semesterName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal outstandingAmount;
    private PaymentStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
