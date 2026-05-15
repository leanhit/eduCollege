package com.educollege.academic.controller;

import com.educollege.academic.dto.GradeSubmissionRequest;
import com.educollege.academic.model.Enrollment;
import com.educollege.academic.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;

/**
 * Grade Controller
 */
@RestController
@RequestMapping("/api/v1/academic/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /**
     * Submit and calculate grades for a student enrollment
     * (Typically used by Teachers)
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Enrollment> submitGrade(@Valid @RequestBody GradeSubmissionRequest request) {
        Enrollment result = gradeService.submitGrade(request);
        return ResponseEntity.ok(result);
    }
}
