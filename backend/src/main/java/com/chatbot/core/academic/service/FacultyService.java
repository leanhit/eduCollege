package com.chatbot.core.academic.service;

import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Faculty Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FacultyService {
    
    private final FacultyRepository facultyRepository;
    
    public Faculty createFaculty(Faculty faculty) {
        System.out.println("Creating faculty: " + faculty.getCode());
        
        if (facultyRepository.existsByCode(faculty.getCode())) {
            throw new RuntimeException("Faculty with code " + faculty.getCode() + " already exists");
        }
        
        Faculty savedFaculty = facultyRepository.save(faculty);
        System.out.println("Faculty created successfully: " + savedFaculty.getCode());
        return savedFaculty;
    }
    
    public Faculty updateFaculty(Long id, Faculty faculty) {
        System.out.println("Updating faculty with id: " + id);
        
        Faculty existingFaculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        // Check if code is being changed and if new code already exists
        if (!existingFaculty.getCode().equals(faculty.getCode()) && 
            facultyRepository.existsByCode(faculty.getCode())) {
            throw new RuntimeException("Faculty with code " + faculty.getCode() + " already exists");
        }
        
        // Update fields
        existingFaculty.setCode(faculty.getCode());
        existingFaculty.setName(faculty.getName());
        existingFaculty.setVietnameseName(faculty.getVietnameseName());
        existingFaculty.setEnglishName(faculty.getEnglishName());
        existingFaculty.setDescription(faculty.getDescription());
        existingFaculty.setContactPhone(faculty.getContactPhone());
        existingFaculty.setContactEmail(faculty.getContactEmail());
        existingFaculty.setOfficeLocation(faculty.getOfficeLocation());
        existingFaculty.setIsActive(faculty.getIsActive());
        
        Faculty updatedFaculty = facultyRepository.save(existingFaculty);
        System.out.println("Faculty updated successfully: " + updatedFaculty.getCode());
        return updatedFaculty;
    }
    
    public void deleteFaculty(Long id) {
        System.out.println("Deleting faculty with id: " + id);
        
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        facultyRepository.delete(faculty);
        System.out.println("Faculty deleted successfully: " + faculty.getCode());
    }
    
    @Transactional(readOnly = true)
    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Faculty> getFacultyByCode(String code) {
        return facultyRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Faculty> getActiveFaculties() {
        return facultyRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Faculty> getInactiveFaculties() {
        return facultyRepository.findByIsActiveFalse();
    }
    
    @Transactional(readOnly = true)
    public List<Faculty> searchFacultiesByName(String name) {
        return facultyRepository.findByNameContaining(name);
    }
    
    @Transactional(readOnly = true)
    public List<Faculty> searchFacultiesByCode(String code) {
        return facultyRepository.findByCodeContaining(code);
    }
    
    public Faculty activateFaculty(Long id) {
        System.out.println("Activating faculty with id: " + id);
        
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        faculty.setIsActive(true);
        Faculty activatedFaculty = facultyRepository.save(faculty);
        System.out.println("Faculty activated successfully: " + activatedFaculty.getCode());
        return activatedFaculty;
    }
    
    public Faculty deactivateFaculty(Long id) {
        System.out.println("Deactivating faculty with id: " + id);
        
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        faculty.setIsActive(false);
        Faculty deactivatedFaculty = facultyRepository.save(faculty);
        System.out.println("Faculty deactivated successfully: " + deactivatedFaculty.getCode());
        return deactivatedFaculty;
    }
    
    @Transactional(readOnly = true)
    public long getActiveFacultyCount() {
        return facultyRepository.countByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public long getInactiveFacultyCount() {
        return facultyRepository.countByIsActiveFalse();
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return facultyRepository.existsByCode(code);
    }
}
