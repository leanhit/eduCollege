package com.chatbot.core.academic.controller;

import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Faculty Controller
 */
@RestController
@RequestMapping("/api/v1/academic/faculties")
@RequiredArgsConstructor
public class FacultyController {
    
    private final FacultyService facultyService;
    
    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, faculty);
        return ResponseEntity.ok(updatedFaculty);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Faculty> getFacultyByCode(@PathVariable String code) {
        return facultyService.getFacultyByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Faculty>> getActiveFaculties() {
        List<Faculty> faculties = facultyService.getActiveFaculties();
        return ResponseEntity.ok(faculties);
    }
    
    @GetMapping("/inactive")
    public ResponseEntity<List<Faculty>> getInactiveFaculties() {
        List<Faculty> faculties = facultyService.getInactiveFaculties();
        return ResponseEntity.ok(faculties);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> searchFaculties(@RequestParam String query) {
        List<Faculty> faculties = facultyService.searchFacultiesByName(query);
        return ResponseEntity.ok(faculties);
    }
    
    @GetMapping("/search/code")
    public ResponseEntity<List<Faculty>> searchFacultiesByCode(@RequestParam String code) {
        List<Faculty> faculties = facultyService.searchFacultiesByCode(code);
        return ResponseEntity.ok(faculties);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Faculty> activateFaculty(@PathVariable Long id) {
        Faculty activatedFaculty = facultyService.activateFaculty(id);
        return ResponseEntity.ok(activatedFaculty);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Faculty> deactivateFaculty(@PathVariable Long id) {
        Faculty deactivatedFaculty = facultyService.deactivateFaculty(id);
        return ResponseEntity.ok(deactivatedFaculty);
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveFacultyCount() {
        Long count = facultyService.getActiveFacultyCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/inactive")
    public ResponseEntity<Long> getInactiveFacultyCount() {
        Long count = facultyService.getInactiveFacultyCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        Boolean exists = facultyService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}
