package com.educollege.user.service;

import com.educollege.user.model.Teacher;
import com.educollege.academic.model.Department;
import com.educollege.user.repository.TeacherRepository;
import com.educollege.academic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Teacher Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    
    public Teacher createTeacher(Teacher teacher) {
        System.out.println("Creating teacher: " + teacher.getTeacherNumber());
        
        // Validate department exists
        Department department = departmentRepository.findById(teacher.getDepartment().getId())
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + teacher.getDepartment().getId()));
        
        teacher.setDepartment(department);
        
        if (teacherRepository.existsByTeacherNumber(teacher.getTeacherNumber())) {
            throw new RuntimeException("Teacher with number " + teacher.getTeacherNumber() + " already exists");
        }
        
        if (teacherRepository.existsByUser_Id(teacher.getUser().getId())) {
            throw new RuntimeException("Teacher with user id " + teacher.getUser().getId() + " already exists");
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        System.out.println("Teacher created successfully: " + savedTeacher.getTeacherNumber());
        return savedTeacher;
    }
    
    public Teacher updateTeacher(Long id, Teacher teacher) {
        System.out.println("Updating teacher with id: " + id);
        
        Teacher existingTeacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        // Validate department if changed
        if (teacher.getDepartment() != null && 
            !existingTeacher.getDepartment().getId().equals(teacher.getDepartment().getId())) {
            Department department = departmentRepository.findById(teacher.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + teacher.getDepartment().getId()));
            existingTeacher.setDepartment(department);
        }
        
        // Check if teacher number is being changed and if new number already exists
        if (!existingTeacher.getTeacherNumber().equals(teacher.getTeacherNumber()) && 
            teacherRepository.existsByTeacherNumber(teacher.getTeacherNumber())) {
            throw new RuntimeException("Teacher with number " + teacher.getTeacherNumber() + " already exists");
        }
        
        // Update fields
        existingTeacher.setTeacherNumber(teacher.getTeacherNumber());
        existingTeacher.setAcademicTitle(teacher.getAcademicTitle());
        existingTeacher.setHireDate(teacher.getHireDate());
        existingTeacher.setSpecialization(teacher.getSpecialization());
        existingTeacher.setResearchInterests(teacher.getResearchInterests());
        existingTeacher.setOfficeLocation(teacher.getOfficeLocation());
        existingTeacher.setOfficePhone(teacher.getOfficePhone());
        existingTeacher.setMobilePhone(teacher.getMobilePhone());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setMaxCoursesPerSemester(teacher.getMaxCoursesPerSemester());
        existingTeacher.setIsAdvisor(teacher.getIsAdvisor());
        existingTeacher.setMaxAdvisees(teacher.getMaxAdvisees());
        existingTeacher.setIsActive(teacher.getIsActive());
        
        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        System.out.println("Teacher updated successfully: " + updatedTeacher.getTeacherNumber());
        return updatedTeacher;
    }
    
    public void deleteTeacher(Long id) {
        System.out.println("Deleting teacher with id: " + id);
        
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        teacherRepository.delete(teacher);
        System.out.println("Teacher deleted successfully: " + teacher.getTeacherNumber());
    }
    
    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherByTeacherNumber(String teacherNumber) {
        return teacherRepository.findByTeacherNumber(teacherNumber);
    }
    
    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherByUserId(Long userId) {
        return teacherRepository.findByUser_Id(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersByDepartmentId(Long departmentId) {
        return teacherRepository.findByDepartmentId(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getActiveTeachers() {
        return teacherRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getAdvisorTeachers() {
        return teacherRepository.findByIsAdvisorTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getActiveTeachersByDepartmentId(Long departmentId) {
        return teacherRepository.findByDepartmentIdAndIsActiveTrue(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getAdvisorTeachersByDepartmentId(Long departmentId) {
        return teacherRepository.findByDepartmentIdAndIsAdvisorTrue(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getAvailableAdvisors() {
        return teacherRepository.findAvailableAdvisors();
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getAvailableTeachers() {
        return teacherRepository.findAvailableTeachers();
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersByAcademicTitle(String academicTitle) {
        return teacherRepository.findByAcademicTitle(academicTitle);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> searchTeachersBySpecialization(String specialization) {
        return teacherRepository.findBySpecializationContaining(specialization);
    }
    
    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> searchTeachersByTeacherNumber(String teacherNumber) {
        return teacherRepository.findByTeacherNumberContaining(teacherNumber);
    }
    
    @Transactional(readOnly = true)
    public List<Teacher> getActiveTeachersByDepartment(Long departmentId) {
        return teacherRepository.findActiveTeachersByDepartment(departmentId);
    }
    
    public Teacher activateTeacher(Long id) {
        System.out.println("Activating teacher with id: " + id);
        
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        teacher.setIsActive(true);
        Teacher activatedTeacher = teacherRepository.save(teacher);
        System.out.println("Teacher activated successfully: " + activatedTeacher.getTeacherNumber());
        return activatedTeacher;
    }
    
    public Teacher deactivateTeacher(Long id) {
        System.out.println("Deactivating teacher with id: " + id);
        
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        teacher.setIsActive(false);
        Teacher deactivatedTeacher = teacherRepository.save(teacher);
        System.out.println("Teacher deactivated successfully: " + deactivatedTeacher.getTeacherNumber());
        return deactivatedTeacher;
    }
    
    @Transactional(readOnly = true)
    public long getTeacherCountByDepartmentId(Long departmentId) {
        return teacherRepository.countByDepartmentId(departmentId);
    }
    
    @Transactional(readOnly = true)
    public long getActiveTeacherCountByDepartmentId(Long departmentId) {
        return teacherRepository.countActiveTeachersByDepartment(departmentId);
    }
    
    @Transactional(readOnly = true)
    public long getActiveTeacherCount() {
        return teacherRepository.countByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public long getAdvisorTeacherCount() {
        return teacherRepository.countByIsAdvisorTrue();
    }
    
    @Transactional(readOnly = true)
    public long getActiveAdvisorTeacherCount() {
        return teacherRepository.countByIsActiveTrueAndIsAdvisorTrue();
    }
    
    @Transactional(readOnly = true)
    public boolean existsByTeacherNumber(String teacherNumber) {
        return teacherRepository.existsByTeacherNumber(teacherNumber);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByUser_Id(Long userId) {
        return teacherRepository.existsByUser_Id(userId);
    }
}
