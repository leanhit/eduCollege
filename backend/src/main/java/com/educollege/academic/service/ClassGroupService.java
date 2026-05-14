package com.educollege.academic.service;

import com.educollege.academic.model.ClassGroup;
import com.educollege.academic.model.Faculty;
import com.educollege.academic.model.Department;
import com.educollege.academic.repository.ClassGroupRepository;
import com.educollege.academic.repository.FacultyRepository;
import com.educollege.academic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Class Group Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassGroupService {
    
    private final ClassGroupRepository classGroupRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    
    public ClassGroup createClassGroup(ClassGroup classGroup) {
        System.out.println("Creating class group: " + classGroup.getCode());
        
        Faculty faculty = facultyRepository.findById(classGroup.getFaculty().getId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + classGroup.getFaculty().getId()));
        
        classGroup.setFaculty(faculty);
        
        if (classGroup.getDepartment() != null) {
            Department department = departmentRepository.findById(classGroup.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + classGroup.getDepartment().getId()));
            classGroup.setDepartment(department);
        }
        
        if (classGroupRepository.existsByCode(classGroup.getCode())) {
            throw new RuntimeException("Class group with code " + classGroup.getCode() + " already exists");
        }
        
        ClassGroup savedClassGroup = classGroupRepository.save(classGroup);
        System.out.println("Class group created successfully: " + savedClassGroup.getCode());
        return savedClassGroup;
    }
    
    public ClassGroup updateClassGroup(Long id, ClassGroup classGroup) {
        System.out.println("Updating class group with id: " + id);
        
        ClassGroup existingClassGroup = classGroupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + id));
        
        if (classGroup.getFaculty() != null && 
            !existingClassGroup.getFaculty().getId().equals(classGroup.getFaculty().getId())) {
            Faculty faculty = facultyRepository.findById(classGroup.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + classGroup.getFaculty().getId()));
            existingClassGroup.setFaculty(faculty);
        }
        
        if (classGroup.getDepartment() != null && 
            (existingClassGroup.getDepartment() == null || 
             !existingClassGroup.getDepartment().getId().equals(classGroup.getDepartment().getId()))) {
            Department department = departmentRepository.findById(classGroup.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + classGroup.getDepartment().getId()));
            existingClassGroup.setDepartment(department);
        }
        
        if (!existingClassGroup.getCode().equals(classGroup.getCode()) && 
            classGroupRepository.existsByCode(classGroup.getCode())) {
            throw new RuntimeException("Class group with code " + classGroup.getCode() + " already exists");
        }
        
        existingClassGroup.setCode(classGroup.getCode());
        existingClassGroup.setName(classGroup.getName());
        existingClassGroup.setEnrollmentYear(classGroup.getEnrollmentYear());
        existingClassGroup.setGraduationYear(classGroup.getGraduationYear());
        existingClassGroup.setMaxStudents(classGroup.getMaxStudents());
        existingClassGroup.setCurrentStudents(classGroup.getCurrentStudents());
        existingClassGroup.setIsActive(classGroup.getIsActive());
        
        ClassGroup updatedClassGroup = classGroupRepository.save(existingClassGroup);
        System.out.println("Class group updated successfully: " + updatedClassGroup.getCode());
        return updatedClassGroup;
    }
    
    public void deleteClassGroup(Long id) {
        System.out.println("Deleting class group with id: " + id);
        
        ClassGroup classGroup = classGroupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + id));
        
        classGroupRepository.delete(classGroup);
        System.out.println("Class group deleted successfully: " + classGroup.getCode());
    }
    
    @Transactional(readOnly = true)
    public Optional<ClassGroup> getClassGroupById(Long id) {
        return classGroupRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<ClassGroup> getClassGroupByCode(String code) {
        return classGroupRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getClassGroupsByFacultyId(Long facultyId) {
        return classGroupRepository.findByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getClassGroupsByDepartmentId(Long departmentId) {
        // ClassGroup doesn't have direct department relationship in repository
        // Return empty list for now
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getActiveClassGroups() {
        return classGroupRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getActiveClassGroupsByFacultyId(Long facultyId) {
        return classGroupRepository.findByFacultyIdAndIsActiveTrue(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getClassGroupsByEnrollmentYear(Integer enrollmentYear) {
        return classGroupRepository.findByEnrollmentYear(enrollmentYear);
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> getClassGroupsByGraduationYear(Integer graduationYear) {
        // ClassGroup doesn't have findByGraduationYear in repository
        // Return empty list for now
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> searchClassGroupsByName(String name) {
        return classGroupRepository.findByNameContaining(name);
    }
    
    @Transactional(readOnly = true)
    public List<ClassGroup> searchClassGroupsByCode(String code) {
        return classGroupRepository.findByCodeContaining(code);
    }
    
    public ClassGroup activateClassGroup(Long id) {
        System.out.println("Activating class group with id: " + id);
        
        ClassGroup classGroup = classGroupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + id));
        
        classGroup.setIsActive(true);
        ClassGroup activatedClassGroup = classGroupRepository.save(classGroup);
        System.out.println("Class group activated successfully: " + activatedClassGroup.getCode());
        return activatedClassGroup;
    }
    
    public ClassGroup deactivateClassGroup(Long id) {
        System.out.println("Deactivating class group with id: " + id);
        
        ClassGroup classGroup = classGroupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + id));
        
        classGroup.setIsActive(false);
        ClassGroup deactivatedClassGroup = classGroupRepository.save(classGroup);
        System.out.println("Class group deactivated successfully: " + deactivatedClassGroup.getCode());
        return deactivatedClassGroup;
    }
    
    @Transactional(readOnly = true)
    public long getClassGroupCountByFacultyId(Long facultyId) {
        return classGroupRepository.countByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public long getActiveClassGroupCountByFacultyId(Long facultyId) {
        return classGroupRepository.countByFacultyIdAndIsActiveTrue(facultyId);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return classGroupRepository.existsByCode(code);
    }
}
