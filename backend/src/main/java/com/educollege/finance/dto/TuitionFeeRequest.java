package com.educollege.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tuition Fee Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuitionFeeRequest {
    
    private Long studentId;
    private Long semesterId;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
}
