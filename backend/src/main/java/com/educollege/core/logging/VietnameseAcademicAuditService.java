package com.educollege.core.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Vietnamese Academic Audit Service
 * Logs all important academic operations for audit and compliance
 */
@Service
@Slf4j
public class VietnameseAcademicAuditService {

    /**
     * Log student registration
     */
    public void logStudentRegistration(Long studentId, String vietnameseId, String facultyCode, String classCode) {
        log.info("STUDENT_REGISTRATION: studentId={}, vietnameseId={}, faculty={}, class={}, timestamp={}",
            studentId, vietnameseId, facultyCode, classCode, LocalDateTime.now());
        
        // In production, store in audit table
        // auditRepository.save(AuditLog.builder()
        //     .action("STUDENT_REGISTRATION")
        //     .entityId(studentId)
        //     .details(String.format("Vietnamese ID: %s, Faculty: %s, Class: %s", vietnameseId, facultyCode, classCode))
        //     .timestamp(LocalDateTime.now())
        //     .build());
    }

    /**
     * Log teacher registration
     */
    public void logTeacherRegistration(Long teacherId, String vietnameseId, String departmentCode) {
        log.info("TEACHER_REGISTRATION: teacherId={}, vietnameseId={}, department={}, timestamp={}",
            teacherId, vietnameseId, departmentCode, LocalDateTime.now());
    }

    /**
     * Log course enrollment
     */
    public void logCourseEnrollment(Long enrollmentId, Long studentId, String studentVietnameseId, Long courseOfferingId, String courseCode) {
        log.info("COURSE_ENROLLMENT: enrollmentId={}, studentId={}, studentVietnameseId={}, courseOfferingId={}, courseCode={}, timestamp={}",
            enrollmentId, studentId, studentVietnameseId, courseOfferingId, courseCode, LocalDateTime.now());
    }

    /**
     * Log course drop
     */
    public void logCourseDrop(Long enrollmentId, Long studentId, String studentVietnameseId, String courseCode, String reason) {
        log.info("COURSE_DROP: enrollmentId={}, studentId={}, studentVietnameseId={}, courseCode={}, reason={}, timestamp={}",
            enrollmentId, studentId, studentVietnameseId, courseCode, reason, LocalDateTime.now());
    }

    /**
     * Log grade submission
     */
    public void logGradeSubmission(Long enrollmentId, String studentVietnameseId, String courseCode, Double grade, String letterGrade, Long gradedBy) {
        log.info("GRADE_SUBMISSION: enrollmentId={}, studentVietnameseId={}, courseCode={}, grade={}, letterGrade={}, gradedBy={}, timestamp={}",
            enrollmentId, studentVietnameseId, courseCode, grade, letterGrade, gradedBy, LocalDateTime.now());
    }

    /**
     * Log grade update
     */
    public void logGradeUpdate(Long enrollmentId, Double oldGrade, Double newGrade, Long updatedBy) {
        log.info("GRADE_UPDATE: enrollmentId={}, oldGrade={}, newGrade={}, updatedBy={}, timestamp={}",
            enrollmentId, oldGrade, newGrade, updatedBy, LocalDateTime.now());
    }

    /**
     * Log tuition payment
     */
    public void logTuitionPayment(Long transactionId, Long studentId, String studentVietnameseId, Double amount, String paymentMethod, String status) {
        log.info("TUITION_PAYMENT: transactionId={}, studentId={}, studentVietnameseId={}, amount={}, method={}, status={}, timestamp={}",
            transactionId, studentId, studentVietnameseId, amount, paymentMethod, status, LocalDateTime.now());
    }

    /**
     * Log advising session
     */
    public void logAdvisingSession(Long sessionId, Long studentId, String studentVietnameseId, Long teacherId, String status) {
        log.info("ADVISING_SESSION: sessionId={}, studentId={}, studentVietnameseId={}, teacherId={}, status={}, timestamp={}",
            sessionId, studentId, studentVietnameseId, teacherId, status, LocalDateTime.now());
    }

    /**
     * Log academic standing change
     */
    public void logAcademicStandingChange(Long studentId, String studentVietnameseId, String oldStanding, String newStanding, String reason) {
        log.info("ACADEMIC_STANDING_CHANGE: studentId={}, studentVietnameseId={}, oldStanding={}, newStanding={}, reason={}, timestamp={}",
            studentId, studentVietnameseId, oldStanding, newStanding, reason, LocalDateTime.now());
    }

    /**
     * Log graduation
     */
    public void logGraduation(Long studentId, String studentVietnameseId, Double finalGpa, Integer totalCredits) {
        log.info("GRADUATION: studentId={}, studentVietnameseId={}, finalGpa={}, totalCredits={}, timestamp={}",
            studentId, studentVietnameseId, finalGpa, totalCredits, LocalDateTime.now());
    }

    /**
     * Log login attempt
     */
    public void logLoginAttempt(String username, String vietnameseId, boolean success, String ipAddress) {
        log.info("LOGIN_ATTEMPT: username={}, vietnameseId={}, success={}, ipAddress={}, timestamp={}",
            username, vietnameseId, success, ipAddress, LocalDateTime.now());
    }

    /**
     * Log failed validation
     */
    public void logValidationFailure(String entityType, String entityId, String validationType, String reason) {
        log.warn("VALIDATION_FAILURE: entityType={}, entityId={}, validationType={}, reason={}, timestamp={}",
            entityType, entityId, validationType, reason, LocalDateTime.now());
    }

    /**
     * Log data access
     */
    public void logDataAccess(String action, String entityType, Long entityId, Long userId, String userRole) {
        log.info("DATA_ACCESS: action={}, entityType={}, entityId={}, userId={}, userRole={}, timestamp={}",
            action, entityType, entityId, userId, userRole, LocalDateTime.now());
    }

    /**
     * Log configuration change
     */
    public void logConfigurationChange(String configKey, String oldValue, String newValue, Long changedBy) {
        log.info("CONFIGURATION_CHANGE: configKey={}, oldValue={}, newValue={}, changedBy={}, timestamp={}",
            configKey, oldValue, newValue, changedBy, LocalDateTime.now());
    }

    /**
     * Log system error
     */
    public void logSystemError(String component, String operation, String errorMessage, String stackTrace) {
        log.error("SYSTEM_ERROR: component={}, operation={}, errorMessage={}, timestamp={}",
            component, operation, errorMessage, LocalDateTime.now());
        
        if (stackTrace != null && stackTrace.length() > 0) {
            log.debug("Stack trace: {}", stackTrace);
        }
    }

    /**
     * Log Vietnamese ID generation
     */
    public void logVietnameseIdGeneration(String idType, String generatedId, String facultyCode, String departmentCode, String classCode) {
        log.info("VIETNAMESE_ID_GENERATION: idType={}, generatedId={}, faculty={}, department={}, class={}, timestamp={}",
            idType, generatedId, facultyCode, departmentCode, classCode, LocalDateTime.now());
    }

    /**
     * Log schedule conflict detection
     */
    public void logScheduleConflict(Long studentId, String studentVietnameseId, String existingSchedule, String newSchedule) {
        log.warn("SCHEDULE_CONFLICT: studentId={}, studentVietnameseId={}, existingSchedule={}, newSchedule={}, timestamp={}",
            studentId, studentVietnameseId, existingSchedule, newSchedule, LocalDateTime.now());
    }

    /**
     * Log prerequisite check failure
     */
    public void logPrerequisiteFailure(Long studentId, String studentVietnameseId, String courseCode, String missingPrerequisite) {
        log.warn("PREREQUISITE_FAILURE: studentId={}, studentVietnameseId={}, courseCode={}, missingPrerequisite={}, timestamp={}",
            studentId, studentVietnameseId, courseCode, missingPrerequisite, LocalDateTime.now());
    }

    /**
     * Log capacity limit reached
     */
    public void logCapacityLimitReached(Long courseOfferingId, String courseCode, Integer currentStudents, Integer maxStudents) {
        log.warn("CAPACITY_LIMIT_REACHED: courseOfferingId={}, courseCode={}, currentStudents={}, maxStudents={}, timestamp={}",
            courseOfferingId, courseCode, currentStudents, maxStudents, LocalDateTime.now());
    }
}
