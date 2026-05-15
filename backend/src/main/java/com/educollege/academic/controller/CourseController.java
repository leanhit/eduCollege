package com.educollege.academic.controller;

import com.educollege.academic.model.Course;
import com.educollege.academic.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Controller
 */
@RestController
@RequestMapping("/api/v1/academic/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String code) {
        return courseService.getCourseByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Course>> getCoursesByDepartmentId(@PathVariable Long departmentId) {
        List<Course> courses = courseService.getCoursesByDepartmentId(departmentId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Course>> getActiveCourses() {
        List<Course> courses = courseService.getActiveCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/department/{departmentId}/active")
    public ResponseEntity<List<Course>> getActiveCoursesByDepartmentId(@PathVariable Long departmentId) {
        List<Course> courses = courseService.getActiveCoursesByDepartmentId(departmentId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active) {
        List<Course> courses = courseService.searchCourses(facultyId, credits, keyword, active);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/search/code")
    public ResponseEntity<List<Course>> searchCoursesByCode(@RequestParam String code) {
        List<Course> courses = courseService.searchCoursesByCode(code);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/credits/{credits}")
    public ResponseEntity<List<Course>> getCoursesByCredits(@PathVariable Integer credits) {
        List<Course> courses = courseService.getCoursesByCredits(credits);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/credits/range")
    public ResponseEntity<List<Course>> getCoursesByCreditsRange(@RequestParam Integer minCredits, @RequestParam Integer maxCredits) {
        List<Course> courses = courseService.getCoursesByCreditsRange(minCredits, maxCredits);
        return ResponseEntity.ok(courses);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Course> activateCourse(@PathVariable Long id) {
        Course activatedCourse = courseService.activateCourse(id);
        return ResponseEntity.ok(activatedCourse);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Course> deactivateCourse(@PathVariable Long id) {
        Course deactivatedCourse = courseService.deactivateCourse(id);
        return ResponseEntity.ok(deactivatedCourse);
    }
    
    @GetMapping("/department/{departmentId}/count")
    public ResponseEntity<Long> getCourseCountByDepartmentId(@PathVariable Long departmentId) {
        Long count = courseService.getCourseCountByDepartmentId(departmentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/department/{departmentId}/count/active")
    public ResponseEntity<Long> getActiveCourseCountByDepartmentId(@PathVariable Long departmentId) {
        Long count = courseService.getActiveCourseCountByDepartmentId(departmentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        Boolean exists = courseService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}
