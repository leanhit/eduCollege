package com.educollege.academic.controller;

import com.educollege.academic.model.ClassGroup;
import com.educollege.academic.service.ClassGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class Group Controller
 */
@RestController
@RequestMapping("/api/v1/academic/class-groups")
@RequiredArgsConstructor
public class ClassGroupController {
    
    private final ClassGroupService classGroupService;
    
    @PostMapping
    public ResponseEntity<ClassGroup> createClassGroup(@RequestBody ClassGroup classGroup) {
        ClassGroup createdClassGroup = classGroupService.createClassGroup(classGroup);
        return ResponseEntity.ok(createdClassGroup);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClassGroup> updateClassGroup(@PathVariable Long id, @RequestBody ClassGroup classGroup) {
        ClassGroup updatedClassGroup = classGroupService.updateClassGroup(id, classGroup);
        return ResponseEntity.ok(updatedClassGroup);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassGroup(@PathVariable Long id) {
        classGroupService.deleteClassGroup(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClassGroup> getClassGroupById(@PathVariable Long id) {
        return classGroupService.getClassGroupById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ClassGroup> getClassGroupByCode(@PathVariable String code) {
        return classGroupService.getClassGroupByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ClassGroup>> getAllClassGroups() {
        List<ClassGroup> classGroups = classGroupService.getAllClassGroups();
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<ClassGroup>> getClassGroupsByFacultyId(@PathVariable Long facultyId) {
        List<ClassGroup> classGroups = classGroupService.getClassGroupsByFacultyId(facultyId);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ClassGroup>> getClassGroupsByDepartmentId(@PathVariable Long departmentId) {
        List<ClassGroup> classGroups = classGroupService.getClassGroupsByDepartmentId(departmentId);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ClassGroup>> getActiveClassGroups() {
        List<ClassGroup> classGroups = classGroupService.getActiveClassGroups();
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/faculty/{facultyId}/active")
    public ResponseEntity<List<ClassGroup>> getActiveClassGroupsByFacultyId(@PathVariable Long facultyId) {
        List<ClassGroup> classGroups = classGroupService.getActiveClassGroupsByFacultyId(facultyId);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/enrollment-year/{year}")
    public ResponseEntity<List<ClassGroup>> getClassGroupsByEnrollmentYear(@PathVariable Integer year) {
        List<ClassGroup> classGroups = classGroupService.getClassGroupsByEnrollmentYear(year);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/graduation-year/{year}")
    public ResponseEntity<List<ClassGroup>> getClassGroupsByGraduationYear(@PathVariable Integer year) {
        List<ClassGroup> classGroups = classGroupService.getClassGroupsByGraduationYear(year);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ClassGroup>> searchClassGroups(@RequestParam String query) {
        List<ClassGroup> classGroups = classGroupService.searchClassGroupsByName(query);
        return ResponseEntity.ok(classGroups);
    }
    
    @GetMapping("/search/code")
    public ResponseEntity<List<ClassGroup>> searchClassGroupsByCode(@RequestParam String code) {
        List<ClassGroup> classGroups = classGroupService.searchClassGroupsByCode(code);
        return ResponseEntity.ok(classGroups);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<ClassGroup> activateClassGroup(@PathVariable Long id) {
        ClassGroup activatedClassGroup = classGroupService.activateClassGroup(id);
        return ResponseEntity.ok(activatedClassGroup);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ClassGroup> deactivateClassGroup(@PathVariable Long id) {
        ClassGroup deactivatedClassGroup = classGroupService.deactivateClassGroup(id);
        return ResponseEntity.ok(deactivatedClassGroup);
    }
    
    @GetMapping("/faculty/{facultyId}/count")
    public ResponseEntity<Long> getClassGroupCountByFacultyId(@PathVariable Long facultyId) {
        Long count = classGroupService.getClassGroupCountByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/faculty/{facultyId}/count/active")
    public ResponseEntity<Long> getActiveClassGroupCountByFacultyId(@PathVariable Long facultyId) {
        Long count = classGroupService.getActiveClassGroupCountByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        Boolean exists = classGroupService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}
