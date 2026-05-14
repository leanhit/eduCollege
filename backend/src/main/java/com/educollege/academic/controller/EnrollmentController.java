package com.educollege.academic.controller;

import com.educollege.academic.model.Enrollment;
import com.educollege.academic.service.EnrollmentService;
import com.educollege.core.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Enrollment Controller
 */
@RestController
@RequestMapping("/api/v1/academic/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(@RequestParam Long studentId, @RequestParam Long courseOfferingId) {
        Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseOfferingId);
        return ResponseEntity.ok(enrollment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        Enrollment updatedEnrollment = enrollmentService.updateEnrollment(id, enrollment);
        return ResponseEntity.ok(updatedEnrollment);
    }
    
    @PutMapping("/{id}/grade")
    public ResponseEntity<Enrollment> gradeEnrollment(@PathVariable Long id, 
                                                        @RequestParam Double overallGrade,
                                                        @RequestParam String letterGrade,
                                                        @RequestParam Double gpaPoints) {
        Enrollment gradedEnrollment = enrollmentService.gradeEnrollment(id, overallGrade, letterGrade, gpaPoints);
        return ResponseEntity.ok(gradedEnrollment);
    }
    
    @PutMapping("/{id}/drop")
    public ResponseEntity<Enrollment> dropEnrollment(@PathVariable Long id) {
        Enrollment droppedEnrollment = enrollmentService.dropEnrollment(id);
        return ResponseEntity.ok(droppedEnrollment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course-offering/{courseOfferingId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseOfferingId(@PathVariable Long courseOfferingId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseOfferingId(courseOfferingId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentIdAndSemester(@PathVariable Long studentId, 
                                                                                   @PathVariable Long semesterId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentIdAndSemester(studentId, semesterId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course-offering/{courseOfferingId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseOfferingIdAndStatus(@PathVariable Long courseOfferingId, 
                                                                                       @PathVariable EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseOfferingIdAndStatus(courseOfferingId, status);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}/course-offering/{courseOfferingId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourseOffering(@PathVariable Long studentId, 
                                                                              @PathVariable Long courseOfferingId) {
        return enrollmentService.getEnrollmentByStudentAndCourseOffering(studentId, courseOfferingId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}/count")
    public ResponseEntity<Long> countEnrollmentsByStudentId(@PathVariable Long studentId) {
        Long count = enrollmentService.countEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/course-offering/{courseOfferingId}/count")
    public ResponseEntity<Long> countEnrollmentsByCourseOfferingId(@PathVariable Long courseOfferingId) {
        Long count = enrollmentService.countEnrollmentsByCourseOfferingId(courseOfferingId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/student/{studentId}/course-offering/{courseOfferingId}/exists")
    public ResponseEntity<Boolean> existsByStudentIdAndCourseOfferingId(@PathVariable Long studentId, 
                                                                         @PathVariable Long courseOfferingId) {
        Boolean exists = enrollmentService.existsByStudentIdAndCourseOfferingId(studentId, courseOfferingId);
        return ResponseEntity.ok(exists);
    }
}
