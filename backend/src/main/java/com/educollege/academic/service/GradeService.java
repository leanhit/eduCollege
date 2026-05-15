package com.educollege.academic.service;

import com.educollege.academic.dto.GradeSubmissionRequest;
import com.educollege.academic.model.Enrollment;
import com.educollege.academic.repository.EnrollmentRepository;
import com.educollege.core.enums.EnrollmentStatus;
import com.educollege.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Grade Service for handling student grading logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GradeService {

    private final EnrollmentRepository enrollmentRepository;
    private final VietnameseIdService vietnameseIdService;
    private final NotificationService notificationService;

    // Default weight settings (can be configured per course in future)
    private static final double ATTENDANCE_WEIGHT = 0.1;
    private static final double ASSIGNMENT_WEIGHT = 0.2;
    private static final double MIDTERM_WEIGHT = 0.3;
    private static final double FINAL_WEIGHT = 0.4;

    /**
     * Submit and calculate overall grade for an enrollment
     */
    public Enrollment submitGrade(GradeSubmissionRequest request) {
        log.info("Submitting grade for enrollment ID: {}", request.getEnrollmentId());

        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Update raw grades
        enrollment.setAttendanceRate(request.getParticipationGrade()); // Using participation as indicator
        enrollment.setParticipationGrade(request.getParticipationGrade());
        enrollment.setAssignmentGrade(request.getAssignmentGrade());
        enrollment.setMidtermGrade(request.getMidtermGrade());
        enrollment.setFinalGrade(request.getFinalGrade());
        enrollment.setNotes(request.getNotes());

        // Calculate weighted average (Overall Grade)
        double overallGrade = (request.getParticipationGrade() * ATTENDANCE_WEIGHT) +
                              (request.getAssignmentGrade() * ASSIGNMENT_WEIGHT) +
                              (request.getMidtermGrade() * MIDTERM_WEIGHT) +
                              (request.getFinalGrade() * FINAL_WEIGHT);

        // Standardize grade (round to 1 decimal place)
        overallGrade = Math.round(overallGrade * 10.0) / 10.0;
        enrollment.setGrade(overallGrade);

        // Convert to Vietnamese GPA scale (4.0) and Letter Grade (A, B, C...)
        double gpaPoints = vietnameseIdService.calculateGpa(overallGrade);
        String letterGrade = vietnameseIdService.gpaToLetterGrade(gpaPoints);

        enrollment.setGpaPoints(gpaPoints);
        enrollment.setLetterGrade(letterGrade);
        enrollment.setGradeSubmissionDate(LocalDateTime.now());

        // Update status based on overall grade
        if (overallGrade >= 5.0) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletionDate(LocalDateTime.now());
        } else {
            enrollment.setStatus(EnrollmentStatus.FAILED);
        }

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send real-time notification to student
        try {
            notificationService.notifyGradeSubmission(
                enrollment.getStudent().getUser().getUsername(),
                enrollment.getCourseOffering().getCourse().getName(),
                overallGrade
            );
        } catch (Exception e) {
            log.error("Failed to send real-time notification: {}", e.getMessage());
        }

        return savedEnrollment;
    }
}
