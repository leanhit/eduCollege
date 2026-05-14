package com.educollege.academic.controller;

import com.educollege.academic.model.Semester;
import com.educollege.academic.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Semester Controller
 */
@RestController
@RequestMapping("/api/v1/academic/semesters")
@RequiredArgsConstructor
public class SemesterController {
    
    private final SemesterService semesterService;
    
    @PostMapping
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester) {
        Semester createdSemester = semesterService.createSemester(semester);
        return ResponseEntity.ok(createdSemester);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@PathVariable Long id, @RequestBody Semester semester) {
        Semester updatedSemester = semesterService.updateSemester(id, semester);
        return ResponseEntity.ok(updatedSemester);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable Long id) {
        return semesterService.getSemesterById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Semester> getSemesterByCode(@PathVariable String code) {
        return semesterService.getSemesterByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters() {
        List<Semester> semesters = semesterService.getAllSemesters();
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Semester>> getActiveSemesters() {
        List<Semester> semesters = semesterService.getActiveSemesters();
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/academic-year/{year}")
    public ResponseEntity<List<Semester>> getSemestersByAcademicYear(@PathVariable String year) {
        List<Semester> semesters = semesterService.getSemestersByAcademicYear(year);
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/current")
    public ResponseEntity<Semester> getCurrentSemester() {
        return semesterService.getCurrentSemester()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/registration-open")
    public ResponseEntity<List<Semester>> getRegistrationOpenSemesters() {
        List<Semester> semesters = semesterService.getRegistrationOpenSemesters();
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Semester>> searchSemesters(@RequestParam String query) {
        List<Semester> semesters = semesterService.searchSemestersByName(query);
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/search/code")
    public ResponseEntity<List<Semester>> searchSemestersByCode(@RequestParam String code) {
        List<Semester> semesters = semesterService.searchSemestersByCode(code);
        return ResponseEntity.ok(semesters);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Semester> activateSemester(@PathVariable Long id) {
        Semester activatedSemester = semesterService.activateSemester(id);
        return ResponseEntity.ok(activatedSemester);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Semester> deactivateSemester(@PathVariable Long id) {
        Semester deactivatedSemester = semesterService.deactivateSemester(id);
        return ResponseEntity.ok(deactivatedSemester);
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveSemesterCount() {
        Long count = semesterService.getActiveSemesterCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        Boolean exists = semesterService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/{id}/registration-open")
    public ResponseEntity<Boolean> isRegistrationOpen(@PathVariable Long id) {
        Boolean isOpen = semesterService.isRegistrationOpen(id);
        return ResponseEntity.ok(isOpen);
    }
}
