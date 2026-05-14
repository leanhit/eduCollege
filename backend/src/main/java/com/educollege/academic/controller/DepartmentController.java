package com.educollege.academic.controller;

import com.educollege.academic.model.Department;
import com.educollege.academic.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department Controller
 */
@RestController
@RequestMapping("/api/v1/academic/departments")
@RequiredArgsConstructor
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.ok(createdDepartment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
        return departmentService.getDepartmentByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Department>> getDepartmentsByFacultyId(@PathVariable Long facultyId) {
        List<Department> departments = departmentService.getDepartmentsByFacultyId(facultyId);
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Department>> getActiveDepartments() {
        List<Department> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/faculty/{facultyId}/active")
    public ResponseEntity<List<Department>> getActiveDepartmentsByFacultyId(@PathVariable Long facultyId) {
        List<Department> departments = departmentService.getActiveDepartmentsByFacultyId(facultyId);
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Department>> searchDepartments(@RequestParam String query) {
        List<Department> departments = departmentService.searchDepartmentsByName(query);
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/search/code")
    public ResponseEntity<List<Department>> searchDepartmentsByCode(@RequestParam String code) {
        List<Department> departments = departmentService.searchDepartmentsByCode(code);
        return ResponseEntity.ok(departments);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Department> activateDepartment(@PathVariable Long id) {
        Department activatedDepartment = departmentService.activateDepartment(id);
        return ResponseEntity.ok(activatedDepartment);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Department> deactivateDepartment(@PathVariable Long id) {
        Department deactivatedDepartment = departmentService.deactivateDepartment(id);
        return ResponseEntity.ok(deactivatedDepartment);
    }
    
    @GetMapping("/faculty/{facultyId}/count")
    public ResponseEntity<Long> getDepartmentCountByFacultyId(@PathVariable Long facultyId) {
        Long count = departmentService.getDepartmentCountByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/faculty/{facultyId}/count/active")
    public ResponseEntity<Long> getActiveDepartmentCountByFacultyId(@PathVariable Long facultyId) {
        Long count = departmentService.getActiveDepartmentCountByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        Boolean exists = departmentService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/code/{code}/faculty/{facultyId}")
    public ResponseEntity<Boolean> existsByCodeAndFacultyId(@PathVariable String code, @PathVariable Long facultyId) {
        Boolean exists = departmentService.existsByCodeAndFacultyId(code, facultyId);
        return ResponseEntity.ok(exists);
    }
}
