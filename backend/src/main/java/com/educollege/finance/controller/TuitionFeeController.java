package com.educollege.finance.controller;

import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.service.TuitionFeeService;
import com.educollege.finance.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Tuition Fee Controller
 */
@RestController
@RequestMapping("/api/v1/finance/tuition-fees")
@RequiredArgsConstructor
public class TuitionFeeController {
    
    private final TuitionFeeService tuitionFeeService;
    
    @PostMapping
    public ResponseEntity<TuitionFee> createTuitionFee(@RequestBody TuitionFee tuitionFee) {
        TuitionFee createdTuitionFee = tuitionFeeService.createTuitionFee(tuitionFee);
        return ResponseEntity.ok(createdTuitionFee);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TuitionFee> updateTuitionFee(@PathVariable Long id, @RequestBody TuitionFee tuitionFee) {
        TuitionFee updatedTuitionFee = tuitionFeeService.updateTuitionFee(id, tuitionFee);
        return ResponseEntity.ok(updatedTuitionFee);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTuitionFee(@PathVariable Long id) {
        tuitionFeeService.deleteTuitionFee(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TuitionFee> getTuitionFeeById(@PathVariable Long id) {
        return tuitionFeeService.getTuitionFeeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<TuitionFee>> getAllTuitionFees() {
        List<TuitionFee> tuitionFees = tuitionFeeService.getAllTuitionFees();
        return ResponseEntity.ok(tuitionFees);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TuitionFee>> getTuitionFeesByStudentId(@PathVariable Long studentId) {
        List<TuitionFee> tuitionFees = tuitionFeeService.getTuitionFeesByStudentId(studentId);
        return ResponseEntity.ok(tuitionFees);
    }
    
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<TuitionFee>> getTuitionFeesBySemesterId(@PathVariable Long semesterId) {
        List<TuitionFee> tuitionFees = tuitionFeeService.getTuitionFeesBySemesterId(semesterId);
        return ResponseEntity.ok(tuitionFees);
    }
    
    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<TuitionFee> getTuitionFeeByStudentIdAndSemesterId(@PathVariable Long studentId, 
                                                                            @PathVariable Long semesterId) {
        return tuitionFeeService.getTuitionFeeByStudentIdAndSemesterId(studentId, semesterId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TuitionFee>> getTuitionFeesByStatus(@PathVariable PaymentStatus status) {
        List<TuitionFee> tuitionFees = tuitionFeeService.getTuitionFeesByStatus(status);
        return ResponseEntity.ok(tuitionFees);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<TuitionFee>> getOverdueTuitionFees() {
        List<TuitionFee> tuitionFees = tuitionFeeService.getOverdueTuitionFees();
        return ResponseEntity.ok(tuitionFees);
    }
    
    @GetMapping("/due-date-range")
    public ResponseEntity<List<TuitionFee>> getTuitionFeesByDueDateRange(@RequestParam LocalDate startDate, 
                                                                          @RequestParam LocalDate endDate) {
        List<TuitionFee> tuitionFees = tuitionFeeService.getTuitionFeesByDueDateRange(startDate, endDate);
        return ResponseEntity.ok(tuitionFees);
    }
    
    @PostMapping("/{id}/payment")
    public ResponseEntity<TuitionFee> recordPayment(@PathVariable Long id, @RequestParam BigDecimal amount) {
        TuitionFee updatedTuitionFee = tuitionFeeService.recordPayment(id, amount);
        return ResponseEntity.ok(updatedTuitionFee);
    }
    
    @PutMapping("/{id}/mark-overdue")
    public ResponseEntity<TuitionFee> markAsOverdue(@PathVariable Long id) {
        TuitionFee updatedTuitionFee = tuitionFeeService.markAsOverdue(id);
        return ResponseEntity.ok(updatedTuitionFee);
    }
    
    @GetMapping("/student/{studentId}/outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstandingByStudentId(@PathVariable Long studentId) {
        BigDecimal total = tuitionFeeService.getTotalOutstandingByStudentId(studentId);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/student/{studentId}/paid")
    public ResponseEntity<BigDecimal> getTotalPaidByStudentId(@PathVariable Long studentId) {
        BigDecimal total = tuitionFeeService.getTotalPaidByStudentId(studentId);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countTuitionFeesByStatus(@PathVariable PaymentStatus status) {
        Long count = tuitionFeeService.countTuitionFeesByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/student/{studentId}/semester/{semesterId}/exists")
    public ResponseEntity<Boolean> existsByStudentIdAndSemesterId(@PathVariable Long studentId, 
                                                                  @PathVariable Long semesterId) {
        Boolean exists = tuitionFeeService.existsByStudentIdAndSemesterId(studentId, semesterId);
        return ResponseEntity.ok(exists);
    }
}
