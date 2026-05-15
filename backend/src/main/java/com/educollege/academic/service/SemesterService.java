package com.educollege.academic.service;

import com.educollege.academic.model.Semester;
import com.educollege.academic.model.Enrollment;
import com.educollege.academic.repository.SemesterRepository;
import com.educollege.academic.repository.EnrollmentRepository;
import com.educollege.user.model.Student;
import com.educollege.user.repository.StudentRepository;
import com.educollege.core.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Semester Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SemesterService {
    
    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    
    public Semester createSemester(Semester semester) {
        System.out.println("Creating semester: " + semester.getCode());
        
        if (semesterRepository.existsByCode(semester.getCode())) {
            throw new RuntimeException("Semester with code " + semester.getCode() + " already exists");
        }
        
        Semester savedSemester = semesterRepository.save(semester);
        System.out.println("Semester created successfully: " + savedSemester.getCode());
        return savedSemester;
    }
    
    public Semester updateSemester(Long id, Semester semester) {
        System.out.println("Updating semester with id: " + id);
        
        Semester existingSemester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        
        if (!existingSemester.getCode().equals(semester.getCode()) && 
            semesterRepository.existsByCode(semester.getCode())) {
            throw new RuntimeException("Semester with code " + semester.getCode() + " already exists");
        }
        
        existingSemester.setCode(semester.getCode());
        existingSemester.setName(semester.getName());
        existingSemester.setAcademicYear(semester.getAcademicYear());
        existingSemester.setSemesterNumber(semester.getSemesterNumber());
        existingSemester.setStartDate(semester.getStartDate());
        existingSemester.setEndDate(semester.getEndDate());
        existingSemester.setRegistrationStart(semester.getRegistrationStart());
        existingSemester.setRegistrationEnd(semester.getRegistrationEnd());
        existingSemester.setIsActive(semester.getIsActive());
        
        Semester updatedSemester = semesterRepository.save(existingSemester);
        System.out.println("Semester updated successfully: " + updatedSemester.getCode());
        return updatedSemester;
    }
    
    public void deleteSemester(Long id) {
        System.out.println("Deleting semester with id: " + id);
        
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        
        semesterRepository.delete(semester);
        System.out.println("Semester deleted successfully: " + semester.getCode());
    }
    
    @Transactional(readOnly = true)
    public Optional<Semester> getSemesterById(Long id) {
        return semesterRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Semester> getSemesterByCode(String code) {
        return semesterRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Semester> getActiveSemesters() {
        return semesterRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Semester> getSemestersByAcademicYear(String academicYear) {
        return semesterRepository.findByAcademicYear(academicYear);
    }
    
    @Transactional(readOnly = true)
    public Optional<Semester> getCurrentSemester() {
        LocalDate today = LocalDate.now();
        return semesterRepository.findCurrentSemester(today);
    }
    
    @Transactional(readOnly = true)
    public List<Semester> getRegistrationOpenSemesters() {
        LocalDate today = LocalDate.now();
        return semesterRepository.findRegistrationOpenSemester(today)
            .map(List::of)
            .orElse(List.of());
    }
    
    @Transactional(readOnly = true)
    public List<Semester> searchSemestersByName(String name) {
        return semesterRepository.findByNameContaining(name);
    }
    
    @Transactional(readOnly = true)
    public List<Semester> searchSemestersByCode(String code) {
        return semesterRepository.findByAcademicYearContaining(code);
    }
    
    public Semester activateSemester(Long id) {
        System.out.println("Activating semester with id: " + id);
        
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        
        semester.setIsActive(true);
        Semester activatedSemester = semesterRepository.save(semester);
        System.out.println("Semester activated successfully: " + activatedSemester.getCode());
        return activatedSemester;
    }
    
    public Semester deactivateSemester(Long id) {
        System.out.println("Deactivating semester with id: " + id);
        
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        
        semester.setIsActive(false);
        Semester deactivatedSemester = semesterRepository.save(semester);
        System.out.println("Semester deactivated successfully: " + deactivatedSemester.getCode());
        return deactivatedSemester;
    }
    
    @Transactional(readOnly = true)
    public long getActiveSemesterCount() {
        return semesterRepository.countByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return semesterRepository.existsByCode(code);
    }
    
    @Transactional(readOnly = true)
    public boolean isRegistrationOpen(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + semesterId));
        
        LocalDate today = LocalDate.now();
        return semester.getRegistrationStart() != null && 
               semester.getRegistrationEnd() != null &&
               !today.isBefore(semester.getRegistrationStart()) && 
               !today.isAfter(semester.getRegistrationEnd());
    }

    /**
     * Close a semester and perform final academic calculations for all students
     */
    public void closeSemester(Long id) {
        log.info("Closing semester with id: {}", id);
        
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Semester not found"));

        // Get all unique students who had enrollments in this semester
        List<Long> studentIds = enrollmentRepository.findStudentIdsBySemesterId(id);
        
        for (Long studentId : studentIds) {
            updateStudentAcademicProgress(studentId, id);
        }

        semester.setIsActive(false);
        semesterRepository.save(semester);
        log.info("Semester {} closed successfully", semester.getCode());
    }

    private void updateStudentAcademicProgress(Long studentId, Long semesterId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return;

        List<Enrollment> semesterEnrollments = enrollmentRepository.findByStudentIdAndSemesterId(studentId, semesterId);
        
        // Calculate Semester GPA
        double weightedGpaSum = 0;
        int semesterCredits = 0;
        int earnedCredits = 0;

        for (Enrollment e : semesterEnrollments) {
            int credits = e.getCourseOffering().getCourse().getCredits();
            if (e.getGpaPoints() != null) {
                weightedGpaSum += e.getGpaPoints() * credits;
                semesterCredits += credits;
                if (e.getStatus() == EnrollmentStatus.COMPLETED) {
                    earnedCredits += credits;
                }
            }
        }

        if (semesterCredits > 0) {
            double semesterGpa = weightedGpaSum / semesterCredits;
            log.info("Student {} - Semester GPA: {}", student.getStudentNumber(), semesterGpa);
            
            // Update Cumulative Stats
            int currentCompleted = student.getCompletedCredits() != null ? student.getCompletedCredits() : 0;
            int currentTotal = student.getTotalCredits() != null ? student.getTotalCredits() : 0;
            double currentCumulativeGpa = student.getCumulativeGpa() != null ? student.getCumulativeGpa() : 0;

            int newTotal = currentTotal + semesterCredits;
            double newCumulativeGpa = ((currentCumulativeGpa * currentTotal) + (semesterGpa * semesterCredits)) / newTotal;

            student.setCompletedCredits(currentCompleted + earnedCredits);
            student.setTotalCredits(newTotal);
            student.setCumulativeGpa(Math.round(newCumulativeGpa * 100.0) / 100.0);
            student.setCurrentGpa(Math.round(semesterGpa * 100.0) / 100.0);
            
            studentRepository.save(student);
        }
    }
}
