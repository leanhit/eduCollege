package com.educollege.report.controller;

import com.educollege.finance.model.TuitionFee;
import com.educollege.report.dto.StudentTranscriptResponse;
import com.educollege.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Report Controller
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Get full transcript for a student
     */
    @GetMapping("/transcript/student/{studentId}")
    public ResponseEntity<StudentTranscriptResponse> getTranscript(@PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getStudentTranscript(studentId));
    }

    /**
     * Get tuition debt report for a specific semester (Admin only)
     */
    @GetMapping("/finance/debt/semester/{semesterId}")
    public ResponseEntity<List<TuitionFee>> getDebtReport(@PathVariable Long semesterId) {
        return ResponseEntity.ok(reportService.getTuitionDebtReport(semesterId));
    }
}
