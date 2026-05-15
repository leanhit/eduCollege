package com.educollege.finance.controller;

import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.service.TuitionFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tuition Fee Controller
 */
@RestController
@RequestMapping("/api/v1/finance/tuition")
@RequiredArgsConstructor
public class TuitionFeeController {

    private final TuitionFeeService tuitionFeeService;

    /**
     * Get tuition fees for a specific student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TuitionFee>> getStudentTuitionFees(@PathVariable Long studentId) {
        return ResponseEntity.ok(tuitionFeeService.getTuitionFeesByStudentId(studentId));
    }

    /**
     * Get tuition fee for a student in a specific semester
     */
    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<TuitionFee> getStudentTuitionBySemester(
            @PathVariable Long studentId, 
            @PathVariable Long semesterId) {
        return tuitionFeeService.getTuitionFeeByStudentIdAndSemesterId(studentId, semesterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Request automatic tuition calculation for a student in a semester
     */
    @PostMapping("/calculate")
    public ResponseEntity<TuitionFee> calculateTuition(
            @RequestParam Long studentId, 
            @RequestParam Long semesterId) {
        TuitionFee result = tuitionFeeService.calculateAndCreateTuition(studentId, semesterId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get all overdue tuition fees (Admin only)
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TuitionFee>> getOverdueFees() {
        return ResponseEntity.ok(tuitionFeeService.getOverdueTuitionFees());
    }
}
