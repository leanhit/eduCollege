package com.educollege.academic.controller;

import com.educollege.academic.model.CourseOffering;
import com.educollege.academic.service.CourseOfferingService;
import com.educollege.core.enums.CourseOfferingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Offering Controller
 */
@RestController
@RequestMapping("/api/v1/academic/course-offerings")
@RequiredArgsConstructor
public class CourseOfferingController {
    
    private final CourseOfferingService courseOfferingService;
    
    @PostMapping
    public ResponseEntity<CourseOffering> createCourseOffering(@RequestBody CourseOffering courseOffering) {
        CourseOffering createdOffering = courseOfferingService.createCourseOffering(courseOffering);
        return ResponseEntity.ok(createdOffering);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseOffering> updateCourseOffering(@PathVariable Long id, @RequestBody CourseOffering courseOffering) {
        CourseOffering updatedOffering = courseOfferingService.updateCourseOffering(id, courseOffering);
        return ResponseEntity.ok(updatedOffering);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseOffering(@PathVariable Long id) {
        courseOfferingService.deleteCourseOffering(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseOffering> getCourseOfferingById(@PathVariable Long id) {
        return courseOfferingService.getCourseOfferingById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<CourseOffering>> getAllCourseOfferings() {
        List<CourseOffering> offerings = courseOfferingService.getAllCourseOfferings();
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsByCourseId(@PathVariable Long courseId) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsByCourseId(courseId);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsBySemesterId(@PathVariable Long semesterId) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsBySemesterId(semesterId);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsByTeacherId(@PathVariable Long teacherId) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsByTeacherId(teacherId);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/course/{courseId}/semester/{semesterId}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsByCourseIdAndSemesterId(@PathVariable Long courseId, 
                                                                                          @PathVariable Long semesterId) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsByCourseIdAndSemesterId(courseId, semesterId);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsByStatus(@PathVariable CourseOfferingStatus status) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsByStatus(status);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/open")
    public ResponseEntity<List<CourseOffering>> getOpenCourseOfferings() {
        List<CourseOffering> offerings = courseOfferingService.getOpenCourseOfferings();
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/semester/{semesterId}/status/{status}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsBySemesterIdAndStatus(@PathVariable Long semesterId, 
                                                                                       @PathVariable CourseOfferingStatus status) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsBySemesterIdAndStatus(semesterId, status);
        return ResponseEntity.ok(offerings);
    }
    
    @GetMapping("/teacher/{teacherId}/semester/{semesterId}")
    public ResponseEntity<List<CourseOffering>> getCourseOfferingsByTeacherIdAndSemesterId(@PathVariable Long teacherId, 
                                                                                          @PathVariable Long semesterId) {
        List<CourseOffering> offerings = courseOfferingService.getCourseOfferingsByTeacherIdAndSemesterId(teacherId, semesterId);
        return ResponseEntity.ok(offerings);
    }
    
    @PutMapping("/{id}/open")
    public ResponseEntity<CourseOffering> openCourseOffering(@PathVariable Long id) {
        CourseOffering openedOffering = courseOfferingService.openCourseOffering(id);
        return ResponseEntity.ok(openedOffering);
    }
    
    @PutMapping("/{id}/close")
    public ResponseEntity<CourseOffering> closeCourseOffering(@PathVariable Long id) {
        CourseOffering closedOffering = courseOfferingService.closeCourseOffering(id);
        return ResponseEntity.ok(closedOffering);
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<CourseOffering> cancelCourseOffering(@PathVariable Long id) {
        CourseOffering cancelledOffering = courseOfferingService.cancelCourseOffering(id);
        return ResponseEntity.ok(cancelledOffering);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<CourseOffering> completeCourseOffering(@PathVariable Long id) {
        CourseOffering completedOffering = courseOfferingService.completeCourseOffering(id);
        return ResponseEntity.ok(completedOffering);
    }
    
    @GetMapping("/semester/{semesterId}/count")
    public ResponseEntity<Long> countCourseOfferingsBySemesterId(@PathVariable Long semesterId) {
        Long count = courseOfferingService.countCourseOfferingsBySemesterId(semesterId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/teacher/{teacherId}/count")
    public ResponseEntity<Long> countCourseOfferingsByTeacherId(@PathVariable Long teacherId) {
        Long count = courseOfferingService.countCourseOfferingsByTeacherId(teacherId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countCourseOfferingsByStatus(@PathVariable CourseOfferingStatus status) {
        Long count = courseOfferingService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
}
