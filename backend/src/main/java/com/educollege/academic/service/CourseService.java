package com.educollege.academic.service;

import com.educollege.academic.model.Course;
import com.educollege.academic.model.Department;
import com.educollege.academic.repository.CourseRepository;
import com.educollege.academic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.educollege.academic.repository.CourseSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Course Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    
    public Course createCourse(Course course) {
        System.out.println("Creating course: " + course.getCode());
        
        // Validate department exists
        Department department = departmentRepository.findById(course.getDepartment().getId())
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + course.getDepartment().getId()));
        
        course.setDepartment(department);
        
        if (courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course with code " + course.getCode() + " already exists");
        }
        
        Course savedCourse = courseRepository.save(course);
        System.out.println("Course created successfully: " + savedCourse.getCode());
        return savedCourse;
    }
    
    public Course updateCourse(Long id, Course course) {
        System.out.println("Updating course with id: " + id);
        
        Course existingCourse = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        // Validate department if changed
        if (course.getDepartment() != null && 
            (existingCourse.getDepartment() == null || 
             !existingCourse.getDepartment().getId().equals(course.getDepartment().getId()))) {
            Department department = departmentRepository.findById(course.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + course.getDepartment().getId()));
            existingCourse.setDepartment(department);
        }
        
        // Check if code is being changed and if new code already exists
        if (!existingCourse.getCode().equals(course.getCode()) && 
            courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course with code " + course.getCode() + " already exists");
        }
        
        // Update fields
        existingCourse.setCode(course.getCode());
        existingCourse.setName(course.getName());
        existingCourse.setVietnameseName(course.getVietnameseName());
        existingCourse.setEnglishName(course.getEnglishName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setCredits(course.getCredits());
        existingCourse.setTheoryHours(course.getTheoryHours());
        existingCourse.setPracticeHours(course.getPracticeHours());
        existingCourse.setCourseType(course.getCourseType());
        existingCourse.setIsActive(course.getIsActive());
        
        Course updatedCourse = courseRepository.save(existingCourse);
        System.out.println("Course updated successfully: " + updatedCourse.getCode());
        return updatedCourse;
    }
    
    public void deleteCourse(Long id) {
        System.out.println("Deleting course with id: " + id);
        
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        courseRepository.delete(course);
        System.out.println("Course deleted successfully: " + course.getCode());
    }
    
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Course> getCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getActiveCourses() {
        return courseRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Course> getActiveCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentIdAndIsActiveTrue(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Course> searchCoursesByName(String name) {
        return courseRepository.findByNameContaining(name);
    }
    
    @Transactional(readOnly = true)
    public List<Course> searchCoursesByCode(String code) {
        return courseRepository.findByCodeContaining(code);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getCoursesByCredits(Integer credits) {
        return courseRepository.findByCredits(credits);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getCoursesByCreditsRange(Integer minCredits, Integer maxCredits) {
        return courseRepository.findByCreditsBetween(minCredits, maxCredits);
    }
    
    
    public Course activateCourse(Long id) {
        System.out.println("Activating course with id: " + id);
        
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        course.setIsActive(true);
        Course activatedCourse = courseRepository.save(course);
        System.out.println("Course activated successfully: " + activatedCourse.getCode());
        return activatedCourse;
    }
    
    public Course deactivateCourse(Long id) {
        System.out.println("Deactivating course with id: " + id);
        
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        course.setIsActive(false);
        Course deactivatedCourse = courseRepository.save(course);
        System.out.println("Course deactivated successfully: " + deactivatedCourse.getCode());
        return deactivatedCourse;
    }
    
    @Transactional(readOnly = true)
    public long getCourseCountByDepartmentId(Long departmentId) {
        return courseRepository.countByDepartmentId(departmentId);
    }
    
    @Transactional(readOnly = true)
    public long getActiveCourseCountByDepartmentId(Long departmentId) {
        return courseRepository.countByDepartmentIdAndIsActiveTrue(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Course> searchCourses(Long facultyId, Integer credits, String keyword, Boolean active) {
        log.info("Searching courses with facultyId={}, credits={}, keyword={}, active={}", 
                 facultyId, credits, keyword, active);

        Specification<Course> spec = Specification.where(CourseSpecification.hasFacultyId(facultyId))
                .and(CourseSpecification.hasCredits(credits))
                .and(CourseSpecification.searchByKeyword(keyword))
                .and(CourseSpecification.isActive(active));

        return courseRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return courseRepository.existsByCode(code);
    }
}
