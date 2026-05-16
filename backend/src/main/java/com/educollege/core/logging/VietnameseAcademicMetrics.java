package com.educollege.core.logging;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Vietnamese Academic Metrics
 * Tracks academic operations metrics for monitoring and analytics
 */
@Component
@Slf4j
public class VietnameseAcademicMetrics {

    private final MeterRegistry meterRegistry;
    
    // Counters
    private final Counter studentRegistrationCounter;
    private final Counter teacherRegistrationCounter;
    private final Counter courseEnrollmentCounter;
    private final Counter courseDropCounter;
    private final Counter gradeSubmissionCounter;
    private final Counter tuitionPaymentCounter;
    private final Counter advisingSessionCounter;
    
    // Timers
    private final Timer enrollmentProcessingTimer;
    private final Timer gradeSubmissionTimer;
    private final Timer paymentProcessingTimer;
    
    public VietnameseAcademicMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize counters
        this.studentRegistrationCounter = Counter.builder("student.registration.count")
            .description("Total student registrations")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.teacherRegistrationCounter = Counter.builder("teacher.registration.count")
            .description("Total teacher registrations")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.courseEnrollmentCounter = Counter.builder("course.enrollment.count")
            .description("Total course enrollments")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.courseDropCounter = Counter.builder("course.drop.count")
            .description("Total course drops")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.gradeSubmissionCounter = Counter.builder("grade.submission.count")
            .description("Total grade submissions")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.tuitionPaymentCounter = Counter.builder("tuition.payment.count")
            .description("Total tuition payments")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.advisingSessionCounter = Counter.builder("advising.session.count")
            .description("Total advising sessions")
            .tag("system", "educollege")
            .register(meterRegistry);
        
        // Initialize timers
        this.enrollmentProcessingTimer = Timer.builder("enrollment.processing.time")
            .description("Time taken to process enrollment")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.gradeSubmissionTimer = Timer.builder("grade.submission.time")
            .description("Time taken to submit grades")
            .tag("system", "educollege")
            .register(meterRegistry);
            
        this.paymentProcessingTimer = Timer.builder("payment.processing.time")
            .description("Time taken to process payment")
            .tag("system", "educollege")
            .register(meterRegistry);
    }
    
    /**
     * Record student registration
     */
    public void recordStudentRegistration(String facultyCode, String classCode) {
        log.info("Recording student registration: faculty={}, class={}", facultyCode, classCode);
        meterRegistry.counter("student.registration.count",
            Tags.of("system", "educollege", "faculty", facultyCode, "class", classCode)).increment();
    }
    
    /**
     * Record teacher registration
     */
    public void recordTeacherRegistration(String departmentCode) {
        log.info("Recording teacher registration: department={}", departmentCode);
        meterRegistry.counter("teacher.registration.count",
            Tags.of("system", "educollege", "department", departmentCode)).increment();
    }
    
    /**
     * Record course enrollment
     */
    public void recordCourseEnrollment(String courseCode, String courseType) {
        log.info("Recording course enrollment: course={}, type={}", courseCode, courseType);
        meterRegistry.counter("course.enrollment.count",
            Tags.of("system", "educollege", "course", courseCode, "type", courseType)).increment();
    }
    
    /**
     * Record course drop
     */
    public void recordCourseDrop(String courseCode, String reason) {
        log.info("Recording course drop: course={}, reason={}", courseCode, reason);
        meterRegistry.counter("course.drop.count",
            Tags.of("system", "educollege", "course", courseCode, "reason", reason)).increment();
    }
    
    /**
     * Record grade submission
     */
    public void recordGradeSubmission(String courseCode, String letterGrade) {
        log.info("Recording grade submission: course={}, grade={}", courseCode, letterGrade);
        meterRegistry.counter("grade.submission.count",
            Tags.of("system", "educollege", "course", courseCode, "grade", letterGrade)).increment();
    }
    
    /**
     * Record tuition payment
     */
    public void recordTuitionPayment(String paymentMethod, String status) {
        log.info("Recording tuition payment: method={}, status={}", paymentMethod, status);
        meterRegistry.counter("tuition.payment.count",
            Tags.of("system", "educollege", "method", paymentMethod, "status", status)).increment();
    }
    
    /**
     * Record advising session
     */
    public void recordAdvisingSession(String semesterCode, String status) {
        log.info("Recording advising session: semester={}, status={}", semesterCode, status);
        meterRegistry.counter("advising.session.count",
            Tags.of("system", "educollege", "semester", semesterCode, "status", status)).increment();
    }
    
    /**
     * Start enrollment processing timer
     */
    public Timer.Sample startEnrollmentTimer() {
        log.debug("Starting enrollment processing timer");
        return Timer.start(meterRegistry);
    }
    
    /**
     * Stop enrollment processing timer
     */
    public void stopEnrollmentTimer(Timer.Sample sample, String facultyCode) {
        log.debug("Stopping enrollment processing timer: faculty={}", facultyCode);
        Timer timer = meterRegistry.timer("enrollment.processing.time",
            Tags.of("system", "educollege", "faculty", facultyCode));
        sample.stop(timer);
    }
    
    /**
     * Start grade submission timer
     */
    public Timer.Sample startGradeSubmissionTimer() {
        log.debug("Starting grade submission timer");
        return Timer.start(meterRegistry);
    }
    
    /**
     * Stop grade submission timer
     */
    public void stopGradeSubmissionTimer(Timer.Sample sample, String courseCode) {
        log.debug("Stopping grade submission timer: course={}", courseCode);
        Timer timer = meterRegistry.timer("grade.submission.time",
            Tags.of("system", "educollege", "course", courseCode));
        sample.stop(timer);
    }
    
    /**
     * Start payment processing timer
     */
    public Timer.Sample startPaymentProcessingTimer() {
        log.debug("Starting payment processing timer");
        return Timer.start(meterRegistry);
    }
    
    /**
     * Stop payment processing timer
     */
    public void stopPaymentProcessingTimer(Timer.Sample sample, String paymentMethod) {
        log.debug("Stopping payment processing timer: method={}", paymentMethod);
        Timer timer = meterRegistry.timer("payment.processing.time",
            Tags.of("system", "educollege", "method", paymentMethod));
        sample.stop(timer);
    }
    
    /**
     * Record custom metric
     */
    public void recordCustomMetric(String metricName, double value, String... tags) {
        log.debug("Recording custom metric: name={}, value={}", metricName, value);
        meterRegistry.gauge(metricName, Tags.of(tags), value);
    }
    
    /**
     * Record GPA distribution
     */
    public void recordGpaDistribution(double gpa, String facultyCode) {
        String gpaRange = getGpaRange(gpa);
        log.debug("Recording GPA distribution: gpa={}, range={}, faculty={}", gpa, gpaRange, facultyCode);
        meterRegistry.counter("gpa.distribution", 
            Tags.of("range", gpaRange, "faculty", facultyCode)).increment();
    }
    
    private String getGpaRange(double gpa) {
        if (gpa >= 3.6) return "honors";
        if (gpa >= 3.0) return "good";
        if (gpa >= 2.0) return "satisfactory";
        return "needs_improvement";
    }
}
