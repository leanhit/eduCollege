package com.chatbot.core.academic.controller;

import com.chatbot.core.academic.model.Teacher;
import com.chatbot.core.academic.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Teacher Controller
 */
@RestController
@RequestMapping("/api/v1/academic/teachers")
@RequiredArgsConstructor
public class TeacherController {
    
    private final TeacherService teacherService;
    
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher createdTeacher = teacherService.createTeacher(teacher);
        return ResponseEntity.ok(createdTeacher);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        return ResponseEntity.ok(updatedTeacher);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{teacherNumber}")
    public ResponseEntity<Teacher> getTeacherByTeacherNumber(@PathVariable String teacherNumber) {
        return teacherService.getTeacherByTeacherNumber(teacherNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Teacher> getTeacherByUserId(@PathVariable Long userId) {
        return teacherService.getTeacherByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Teacher>> getTeachersByDepartmentId(@PathVariable Long departmentId) {
        List<Teacher> teachers = teacherService.getTeachersByDepartmentId(departmentId);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Teacher>> getActiveTeachers() {
        List<Teacher> teachers = teacherService.getActiveTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/advisors")
    public ResponseEntity<List<Teacher>> getAdvisorTeachers() {
        List<Teacher> teachers = teacherService.getAdvisorTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/department/{departmentId}/active")
    public ResponseEntity<List<Teacher>> getActiveTeachersByDepartmentId(@PathVariable Long departmentId) {
        List<Teacher> teachers = teacherService.getActiveTeachersByDepartmentId(departmentId);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/department/{departmentId}/advisors")
    public ResponseEntity<List<Teacher>> getAdvisorTeachersByDepartmentId(@PathVariable Long departmentId) {
        List<Teacher> teachers = teacherService.getAdvisorTeachersByDepartmentId(departmentId);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/available/advisors")
    public ResponseEntity<List<Teacher>> getAvailableAdvisors() {
        List<Teacher> teachers = teacherService.getAvailableAdvisors();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/available/teachers")
    public ResponseEntity<List<Teacher>> getAvailableTeachers() {
        List<Teacher> teachers = teacherService.getAvailableTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/title/{academicTitle}")
    public ResponseEntity<List<Teacher>> getTeachersByAcademicTitle(@PathVariable String academicTitle) {
        List<Teacher> teachers = teacherService.getTeachersByAcademicTitle(academicTitle);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/search/specialization")
    public ResponseEntity<List<Teacher>> searchTeachersBySpecialization(@RequestParam String specialization) {
        List<Teacher> teachers = teacherService.searchTeachersBySpecialization(specialization);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Teacher> getTeacherByEmail(@PathVariable String email) {
        return teacherService.getTeacherByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search/number")
    public ResponseEntity<List<Teacher>> searchTeachersByTeacherNumber(@RequestParam String teacherNumber) {
        List<Teacher> teachers = teacherService.searchTeachersByTeacherNumber(teacherNumber);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/department/{departmentId}/active/teachers")
    public ResponseEntity<List<Teacher>> getActiveTeachersByDepartment(@PathVariable Long departmentId) {
        List<Teacher> teachers = teacherService.getActiveTeachersByDepartment(departmentId);
        return ResponseEntity.ok(teachers);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Teacher> activateTeacher(@PathVariable Long id) {
        Teacher activatedTeacher = teacherService.activateTeacher(id);
        return ResponseEntity.ok(activatedTeacher);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Teacher> deactivateTeacher(@PathVariable Long id) {
        Teacher deactivatedTeacher = teacherService.deactivateTeacher(id);
        return ResponseEntity.ok(deactivatedTeacher);
    }
    
    @GetMapping("/department/{departmentId}/count")
    public ResponseEntity<Long> getTeacherCountByDepartmentId(@PathVariable Long departmentId) {
        Long count = teacherService.getTeacherCountByDepartmentId(departmentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/department/{departmentId}/count/active")
    public ResponseEntity<Long> getActiveTeacherCountByDepartmentId(@PathVariable Long departmentId) {
        Long count = teacherService.getActiveTeacherCountByDepartmentId(departmentId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveTeacherCount() {
        Long count = teacherService.getActiveTeacherCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/advisors")
    public ResponseEntity<Long> getAdvisorTeacherCount() {
        Long count = teacherService.getAdvisorTeacherCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/active-advisors")
    public ResponseEntity<Long> getActiveAdvisorTeacherCount() {
        Long count = teacherService.getActiveAdvisorTeacherCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/number/{teacherNumber}")
    public ResponseEntity<Boolean> existsByTeacherNumber(@PathVariable String teacherNumber) {
        Boolean exists = teacherService.existsByTeacherNumber(teacherNumber);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId) {
        Boolean exists = teacherService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }
}
