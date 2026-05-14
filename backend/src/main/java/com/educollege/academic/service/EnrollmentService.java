package com.educollege.academic.service;

import com.educollege.academic.model.CourseOffering;
import com.educollege.academic.model.Enrollment;
import com.educollege.academic.model.Semester;
import com.educollege.academic.repository.EnrollmentRepository;
import com.educollege.academic.repository.CourseOfferingRepository;
import com.educollege.user.model.Student;
import com.educollege.user.repository.StudentRepository;
import com.educollege.core.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final VietnameseAcademicValidationService validationService;
    
    public Enrollment enrollStudent(Long studentId, Long courseOfferingId) {
        System.out.println("Enrolling student " + studentId + " in course offering " + courseOfferingId);
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + courseOfferingId));
        
        // Validate enrollment
        validationService.validateAcademicStanding(student);
        validationService.validateEnrollmentCapacity(courseOffering.getCurrentStudents(), courseOffering.getMaxStudents());
        validationService.validatePrerequisites(student, courseOffering.getCourse().getCode(), 
            courseOffering.getCourse().getPrerequisites());
        validationService.validateScheduleConflict(student, courseOffering.getSchedule());
        
        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseOfferingId(studentId, courseOfferingId)) {
            throw new RuntimeException("Student is already enrolled in this course offering");
        }
        
        Enrollment enrollment = Enrollment.builder()
            .student(student)
            .courseOffering(courseOffering)
            .status(EnrollmentStatus.ENROLLED)
            .enrollmentDate(LocalDateTime.now())
            .attendanceRate(0.0)
            .build();
        
        // Update course offering student count
        courseOffering.setCurrentStudents(courseOffering.getCurrentStudents() + 1);
        courseOfferingRepository.save(courseOffering);
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        System.out.println("Student enrolled successfully");
        return savedEnrollment;
    }
    
    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        System.out.println("Updating enrollment with id: " + id);
        
        Enrollment existingEnrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        validationService.validateGrade(enrollment.getGrade());
        validationService.validateAttendance(enrollment.getAttendanceRate());
        
        existingEnrollment.setStatus(enrollment.getStatus());
        existingEnrollment.setGrade(enrollment.getGrade());
        existingEnrollment.setLetterGrade(enrollment.getLetterGrade());
        existingEnrollment.setGpaPoints(enrollment.getGpaPoints());
        existingEnrollment.setMidtermGrade(enrollment.getMidtermGrade());
        existingEnrollment.setFinalGrade(enrollment.getFinalGrade());
        existingEnrollment.setAssignmentGrade(enrollment.getAssignmentGrade());
        existingEnrollment.setParticipationGrade(enrollment.getParticipationGrade());
        existingEnrollment.setAttendanceRate(enrollment.getAttendanceRate());
        existingEnrollment.setNotes(enrollment.getNotes());
        
        Enrollment updatedEnrollment = enrollmentRepository.save(existingEnrollment);
        System.out.println("Enrollment updated successfully");
        return updatedEnrollment;
    }
    
    public Enrollment gradeEnrollment(Long id, Double overallGrade, String letterGrade, Double gpaPoints) {
        System.out.println("Grading enrollment with id: " + id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        validationService.validateGrade(overallGrade);
        
        enrollment.setGrade(overallGrade);
        enrollment.setLetterGrade(letterGrade);
        enrollment.setGpaPoints(gpaPoints);
        
        if (overallGrade >= 5.0) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
        } else {
            enrollment.setStatus(EnrollmentStatus.FAILED);
        }
        
        Enrollment gradedEnrollment = enrollmentRepository.save(enrollment);
        System.out.println("Enrollment graded successfully");
        return gradedEnrollment;
    }
    
    public Enrollment dropEnrollment(Long id) {
        System.out.println("Dropping enrollment with id: " + id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        
        // Update course offering student count
        CourseOffering courseOffering = enrollment.getCourseOffering();
        if (courseOffering.getCurrentStudents() > 0) {
            courseOffering.setCurrentStudents(courseOffering.getCurrentStudents() - 1);
            courseOfferingRepository.save(courseOffering);
        }
        
        Enrollment droppedEnrollment = enrollmentRepository.save(enrollment);
        System.out.println("Enrollment dropped successfully");
        return droppedEnrollment;
    }
    
    public void deleteEnrollment(Long id) {
        System.out.println("Deleting enrollment with id: " + id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        enrollmentRepository.delete(enrollment);
        System.out.println("Enrollment deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourseOfferingId(Long courseOfferingId) {
        return enrollmentRepository.findByCourseOfferingId(courseOfferingId);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudentIdAndSemester(Long studentId, Long semesterId) {
        return enrollmentRepository.findByStudentIdAndSemesterId(studentId, semesterId);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourseOfferingIdAndStatus(Long courseOfferingId, EnrollmentStatus status) {
        return enrollmentRepository.findByCourseOfferingIdAndStatus(courseOfferingId, status);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Optional<Enrollment> getEnrollmentByStudentAndCourseOffering(Long studentId, Long courseOfferingId) {
        return enrollmentRepository.findByStudentIdAndCourseOfferingId(studentId, courseOfferingId);
    }
    
    @Transactional(readOnly = true)
    public long countEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }
    
    @Transactional(readOnly = true)
    public long countEnrollmentsByCourseOfferingId(Long courseOfferingId) {
        return enrollmentRepository.countByCourseOfferingId(courseOfferingId);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByStudentIdAndCourseOfferingId(Long studentId, Long courseOfferingId) {
        return enrollmentRepository.existsByStudentIdAndCourseOfferingId(studentId, courseOfferingId);
    }
}
