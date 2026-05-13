package com.chatbot.core.academic.service;

import com.chatbot.core.academic.model.Department;
import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.repository.DepartmentRepository;
import com.chatbot.core.academic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Department Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    
    public Department createDepartment(Department department) {
        System.out.println("Creating department: " + department.getCode());
        
        // Validate faculty exists
        Faculty faculty = facultyRepository.findById(department.getFaculty().getId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + department.getFaculty().getId()));
        
        department.setFaculty(faculty);
        
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new RuntimeException("Department with code " + department.getCode() + " already exists");
        }
        
        Department savedDepartment = departmentRepository.save(department);
        System.out.println("Department created successfully: " + savedDepartment.getCode());
        return savedDepartment;
    }
    
    public Department updateDepartment(Long id, Department department) {
        System.out.println("Updating department with id: " + id);
        
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        // Validate faculty if changed
        if (department.getFaculty() != null && 
            !existingDepartment.getFaculty().getId().equals(department.getFaculty().getId())) {
            Faculty faculty = facultyRepository.findById(department.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + department.getFaculty().getId()));
            existingDepartment.setFaculty(faculty);
        }
        
        // Check if code is being changed and if new code already exists
        if (!existingDepartment.getCode().equals(department.getCode()) && 
            departmentRepository.existsByCode(department.getCode())) {
            throw new RuntimeException("Department with code " + department.getCode() + " already exists");
        }
        
        // Update fields
        existingDepartment.setCode(department.getCode());
        existingDepartment.setName(department.getName());
        existingDepartment.setVietnameseName(department.getVietnameseName());
        existingDepartment.setEnglishName(department.getEnglishName());
        existingDepartment.setDescription(department.getDescription());
        existingDepartment.setContactPhone(department.getContactPhone());
        existingDepartment.setContactEmail(department.getContactEmail());
        existingDepartment.setOfficeLocation(department.getOfficeLocation());
        existingDepartment.setHeadOfDepartment(department.getHeadOfDepartment());
        existingDepartment.setIsActive(department.getIsActive());
        
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        System.out.println("Department updated successfully: " + updatedDepartment.getCode());
        return updatedDepartment;
    }
    
    public void deleteDepartment(Long id) {
        System.out.println("Deleting department with id: " + id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        departmentRepository.delete(department);
        System.out.println("Department deleted successfully: " + department.getCode());
    }
    
    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsByFacultyId(Long facultyId) {
        return departmentRepository.findByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Department> getActiveDepartmentsByFacultyId(Long facultyId) {
        return departmentRepository.findByFacultyIdAndIsActiveTrue(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<Department> searchDepartmentsByName(String name) {
        return departmentRepository.findByNameContaining(name);
    }
    
    @Transactional(readOnly = true)
    public List<Department> searchDepartmentsByCode(String code) {
        return departmentRepository.findByCodeContaining(code);
    }
    
    public Department activateDepartment(Long id) {
        System.out.println("Activating department with id: " + id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        department.setIsActive(true);
        Department activatedDepartment = departmentRepository.save(department);
        System.out.println("Department activated successfully: " + activatedDepartment.getCode());
        return activatedDepartment;
    }
    
    public Department deactivateDepartment(Long id) {
        System.out.println("Deactivating department with id: " + id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        department.setIsActive(false);
        Department deactivatedDepartment = departmentRepository.save(department);
        System.out.println("Department deactivated successfully: " + deactivatedDepartment.getCode());
        return deactivatedDepartment;
    }
    
    @Transactional(readOnly = true)
    public long getDepartmentCountByFacultyId(Long facultyId) {
        return departmentRepository.countByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public long getActiveDepartmentCountByFacultyId(Long facultyId) {
        return departmentRepository.countByFacultyIdAndIsActiveTrue(facultyId);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCodeAndFacultyId(String code, Long facultyId) {
        return departmentRepository.existsByCodeAndFacultyId(code, facultyId);
    }
}
