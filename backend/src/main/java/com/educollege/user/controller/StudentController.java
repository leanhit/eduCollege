package com.educollege.user.controller;

import com.educollege.user.model.Student;
import com.educollege.user.service.StudentService;
import com.educollege.core.enums.StudentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Student Controller
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentService studentService;
    
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<Student> getStudentByStudentNumber(@PathVariable String studentNumber) {
        return studentService.getStudentByStudentNumber(studentNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long classGroupId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        List<Student> students = studentService.searchStudents(facultyId, classGroupId, year, status, keyword);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Student> getStudentByUserId(@PathVariable Long userId) {
        return studentService.getStudentByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Student>> getStudentsByFacultyId(@PathVariable Long facultyId) {
        List<Student> students = studentService.getStudentsByFacultyId(facultyId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Student>> getStudentsByDepartmentId(@PathVariable Long departmentId) {
        List<Student> students = studentService.getStudentsByDepartmentId(departmentId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/class-group/{classGroupId}")
    public ResponseEntity<List<Student>> getStudentsByClassGroupId(@PathVariable Long classGroupId) {
        List<Student> students = studentService.getStudentsByClassGroupId(classGroupId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/enrollment-year/{year}")
    public ResponseEntity<List<Student>> getStudentsByEnrollmentYear(@PathVariable Integer year) {
        List<Student> students = studentService.getStudentsByEnrollmentYear(year);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/graduation-year/{year}")
    public ResponseEntity<List<Student>> getStudentsByGraduationYear(@PathVariable Integer year) {
        List<Student> students = studentService.getStudentsByGraduationYear(year);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Student>> getStudentsByStudentStatus(@PathVariable StudentStatus status) {
        List<Student> students = studentService.getStudentsByStudentStatus(status);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/academic-standing/{standing}")
    public ResponseEntity<List<Student>> getStudentsByAcademicStanding(@PathVariable String standing) {
        List<Student> students = studentService.getStudentsByAcademicStanding(standing);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/advisor/{advisorId}")
    public ResponseEntity<List<Student>> getStudentsByAdvisorId(@PathVariable Long advisorId) {
        List<Student> students = studentService.getStudentsByAdvisorId(advisorId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Student>> getActiveStudents() {
        List<Student> students = studentService.getActiveStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/faculty/{facultyId}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByFacultyId(@PathVariable Long facultyId) {
        List<Student> students = studentService.getActiveStudentsByFacultyId(facultyId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/class-group/{classGroupId}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByClassGroupId(@PathVariable Long classGroupId) {
        List<Student> students = studentService.getActiveStudentsByClassGroupId(classGroupId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/enrollment-year/{year}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByEnrollmentYear(@PathVariable Integer year) {
        List<Student> students = studentService.getActiveStudentsByEnrollmentYear(year);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/status/{status}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByStudentStatus(@PathVariable StudentStatus status) {
        List<Student> students = studentService.getActiveStudentsByStudentStatus(status);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/search/number")
    public ResponseEntity<List<Student>> searchStudentsByStudentNumber(@RequestParam String studentNumber) {
        List<Student> students = studentService.searchStudentsByStudentNumber(studentNumber);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/faculty/{facultyId}/year/{year}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByFacultyAndYear(@PathVariable Long facultyId, 
                                                                          @PathVariable Integer year) {
        List<Student> students = studentService.getActiveStudentsByFacultyAndYear(facultyId, year);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/class-group/{classGroupId}/active/students")
    public ResponseEntity<List<Student>> getActiveStudentsByClassGroup(@PathVariable Long classGroupId) {
        List<Student> students = studentService.getActiveStudentsByClassGroup(classGroupId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/advisor/{advisorId}/advisees")
    public ResponseEntity<List<Student>> getActiveAdvisees(@PathVariable Long advisorId) {
        List<Student> students = studentService.getActiveAdvisees(advisorId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/gpa-range")
    public ResponseEntity<List<Student>> getStudentsByGpaRange(@RequestParam Double minGpa, @RequestParam Double maxGpa) {
        List<Student> students = studentService.getStudentsByGpaRange(minGpa, maxGpa);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/academic-standing/{standing}/active")
    public ResponseEntity<List<Student>> getActiveStudentsByAcademicStanding(@PathVariable String standing) {
        List<Student> students = studentService.getActiveStudentsByAcademicStanding(standing);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/faculty/{facultyId}/count/active")
    public ResponseEntity<Long> countActiveStudentsByFaculty(@PathVariable Long facultyId) {
        Long count = studentService.countActiveStudentsByFaculty(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/class-group/{classGroupId}/count/active")
    public ResponseEntity<Long> countActiveStudentsByClassGroup(@PathVariable Long classGroupId) {
        Long count = studentService.countActiveStudentsByClassGroup(classGroupId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/advisor/{advisorId}/count/advisees")
    public ResponseEntity<Long> countActiveAdvisees(@PathVariable Long advisorId) {
        Long count = studentService.countActiveAdvisees(advisorId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/faculty/{facultyId}/average-gpa")
    public ResponseEntity<Double> getAverageGpaByFaculty(@PathVariable Long facultyId) {
        Double average = studentService.getAverageGpaByFaculty(facultyId);
        return ResponseEntity.ok(average);
    }
    
    @GetMapping("/status/{status}/active/students")
    public ResponseEntity<List<Student>> getActiveStudentsByStatus(@PathVariable StudentStatus status) {
        List<Student> students = studentService.getActiveStudentsByStatus(status);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/faculty/{facultyId}/count")
    public ResponseEntity<Long> countByFacultyId(@PathVariable Long facultyId) {
        Long count = studentService.countByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/class-group/{classGroupId}/count")
    public ResponseEntity<Long> countByClassGroupId(@PathVariable Long classGroupId) {
        Long count = studentService.countByClassGroupId(classGroupId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/enrollment-year/{year}/count")
    public ResponseEntity<Long> countByEnrollmentYear(@PathVariable Integer year) {
        Long count = studentService.countByEnrollmentYear(year);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/graduation-year/{year}/count")
    public ResponseEntity<Long> countByGraduationYear(@PathVariable Integer year) {
        Long count = studentService.countByGraduationYear(year);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countByStudentStatus(@PathVariable StudentStatus status) {
        Long count = studentService.countByStudentStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/academic-standing/{standing}/count")
    public ResponseEntity<Long> countByAcademicStanding(@PathVariable String standing) {
        Long count = studentService.countByAcademicStanding(standing);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/advisor/{advisorId}/count")
    public ResponseEntity<Long> countByAdvisorId(@PathVariable Long advisorId) {
        Long count = studentService.countByAdvisorId(advisorId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Long> countByIsActiveTrue() {
        Long count = studentService.countByIsActiveTrue();
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Student> activateStudent(@PathVariable Long id) {
        Student activatedStudent = studentService.activateStudent(id);
        return ResponseEntity.ok(activatedStudent);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Student> deactivateStudent(@PathVariable Long id) {
        Student deactivatedStudent = studentService.deactivateStudent(id);
        return ResponseEntity.ok(deactivatedStudent);
    }
    
    @PutMapping("/{id}/assign-advisor")
    public ResponseEntity<Student> assignAdvisor(@PathVariable Long id, @RequestParam Long advisorId) {
        Student updatedStudent = studentService.assignAdvisor(id, advisorId);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @PutMapping("/{id}/update-gpa")
    public ResponseEntity<Student> updateGpa(@PathVariable Long id, 
                                             @RequestParam Double currentGpa, 
                                             @RequestParam Double cumulativeGpa) {
        Student updatedStudent = studentService.updateGpa(id, currentGpa, cumulativeGpa);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @PutMapping("/{id}/update-credits")
    public ResponseEntity<Student> updateCredits(@PathVariable Long id, 
                                                @RequestParam Integer totalCredits, 
                                                @RequestParam Integer completedCredits, 
                                                @RequestParam Integer failedCredits) {
        Student updatedStudent = studentService.updateCredits(id, totalCredits, completedCredits, failedCredits);
        return ResponseEntity.ok(updatedStudent);
    }
}
