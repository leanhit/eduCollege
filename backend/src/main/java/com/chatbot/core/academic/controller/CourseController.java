package com.chatbot.core.academic.controller;

import com.chatbot.core.academic.model.Course;
import com.chatbot.core.academic.service.CourseService;
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
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Course>> getCoursesByFacultyId(@PathVariable Long facultyId) {
        List<Course> courses = courseService.getCoursesByFacultyId(facultyId);
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
    
    @GetMapping("/elective")
    public ResponseEntity<List<Course>> getElectiveCourses() {
        List<Course> courses = courseService.getElectiveCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/required")
    public ResponseEntity<List<Course>> getRequiredCourses() {
        List<Course> courses = courseService.getRequiredCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/faculty/{facultyId}/active")
    public ResponseEntity<List<Course>> getActiveCoursesByFacultyId(@PathVariable Long facultyId) {
        List<Course> courses = courseService.getActiveCoursesByFacultyId(facultyId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String query) {
        List<Course> courses = courseService.searchCoursesByName(query);
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
    
    @GetMapping("/prerequisite/{prerequisiteCode}")
    public ResponseEntity<List<Course>> getCoursesByPrerequisite(@PathVariable String prerequisiteCode) {
        List<Course> courses = courseService.getCoursesByPrerequisite(prerequisiteCode);
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
    
    @GetMapping("/faculty/{facultyId}/count")
    public ResponseEntity<Long> getCourseCountByFacultyId(@PathVariable Long facultyId) {
        Long count = courseService.getCourseCountByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/department/{departmentId}/count")
    public ResponseEntity<Long> getCourseCountByDepartmentId(@PathVariable Long departmentId) {
        Long count = courseService.getCourseCountByDepartmentId(departmentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/faculty/{facultyId}/count/active")
    public ResponseEntity<Long> getActiveCourseCountByFacultyId(@PathVariable Long facultyId) {
        Long count = courseService.getActiveCourseCountByFacultyId(facultyId);
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
    
    @GetMapping("/exists/code/{code}/faculty/{facultyId}")
    public ResponseEntity<Boolean> existsByCodeAndFacultyId(@PathVariable String code, @PathVariable Long facultyId) {
        Boolean exists = courseService.existsByCodeAndFacultyId(code, facultyId);
        return ResponseEntity.ok(exists);
    }
}
