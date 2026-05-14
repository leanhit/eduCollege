package com.educollege.academic.service;

import com.educollege.user.model.Student;
import com.educollege.academic.model.Semester;
import com.educollege.user.repository.StudentRepository;
import com.educollege.academic.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Vietnamese Academic Validation Service
 * Validates academic rules according to Vietnamese university standards
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VietnameseAcademicValidationService {
    
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final VietnameseIdService vietnameseIdService;
    
    /**
     * Validate Vietnamese Student ID format
     */
    public void validateStudentId(String studentId) {
        System.out.println("Validating Vietnamese Student ID: " + studentId);
        
        if (!vietnameseIdService.isValidStudentId(studentId)) {
            throw new RuntimeException("Invalid Vietnamese student ID format. Expected format: SVYYFACULTYSEQUENCE");
        }
        
        System.out.println("Vietnamese Student ID validation passed");
    }
    
    /**
     * Validate Vietnamese Teacher ID format
     */
    public void validateTeacherId(String teacherId) {
        System.out.println("Validating Vietnamese Teacher ID: " + teacherId);
        
        if (!vietnameseIdService.isValidTeacherId(teacherId)) {
            throw new RuntimeException("Invalid Vietnamese teacher ID format. Expected format: GVDEPARTMENTSEQUENCE");
        }
        
        System.out.println("Vietnamese Teacher ID validation passed");
    }
    
    /**
     * Validate student academic standing
     */
    public void validateAcademicStanding(Student student) {
        System.out.println("Validating academic standing for student: " + student.getStudentNumber());
        
        double gpa = student.getCurrentGpa() != null ? student.getCurrentGpa() : 0.0;
        int failedCredits = student.getFailedCredits() != null ? student.getFailedCredits() : 0;
        
        // Vietnamese academic standing rules
        if (gpa < 2.0) {
            throw new RuntimeException("GPA below minimum requirement (2.0). Current GPA: " + gpa);
        }
        
        if (failedCredits > 12) {
            throw new RuntimeException("Too many failed credits (max 12). Current failed credits: " + failedCredits);
        }
        
        System.out.println("Academic standing validation passed");
    }
    
    /**
     * Validate course load for student
     */
    public void validateCourseLoad(Student student, Semester semester) {
        System.out.println("Validating course load for student: " + student.getStudentNumber() + ", semester: " + semester.getCode());
        
        int currentCredits = getCurrentCredits(student, semester);
        int maxCredits = getMaxCreditsByLevel(student.getEnrollmentYear());
        
        if (currentCredits > maxCredits) {
            throw new RuntimeException("Course load exceeds maximum allowed. Current: " + currentCredits + ", Max: " + maxCredits);
        }
        
        System.out.println("Course load validation passed");
    }
    
    /**
     * Validate enrollment prerequisites
     */
    public void validatePrerequisites(Student student, String courseCode, String prerequisites) {
        System.out.println("Validating prerequisites for student: " + student.getStudentNumber() + ", course: " + courseCode);
        
        if (prerequisites == null || prerequisites.trim().isEmpty()) {
            System.out.println("No prerequisites to validate");
            return;
        }
        
        String[] prereqArray = prerequisites.split(",");
        for (String prereq : prereqArray) {
            prereq = prereq.trim();
            if (!hasCompletedCourse(student, prereq)) {
                throw new RuntimeException("Student has not completed prerequisite: " + prereq);
            }
        }
        
        System.out.println("Prerequisites validation passed");
    }
    
    /**
     * Validate enrollment capacity
     */
    public void validateEnrollmentCapacity(int currentStudents, int maxStudents) {
        System.out.println("Validating enrollment capacity: " + currentStudents + "/" + maxStudents);
        
        if (currentStudents >= maxStudents) {
            throw new RuntimeException("Course is full. Current students: " + currentStudents + ", Max students: " + maxStudents);
        }
        
        System.out.println("Enrollment capacity validation passed");
    }
    
    /**
     * Validate schedule conflicts
     */
    public void validateScheduleConflict(Student student, String newCourseSchedule) {
        System.out.println("Validating schedule conflicts for student: " + student.getStudentNumber());
        
        // In production, this would check against student's current course schedules
        // For now, implement basic validation
        if (hasScheduleConflict(student, newCourseSchedule)) {
            throw new RuntimeException("Schedule conflict detected");
        }
        
        System.out.println("Schedule conflict validation passed");
    }
    
    /**
     * Validate credit accumulation
     */
    public void validateCreditAccumulation(Student student) {
        System.out.println("Validating credit accumulation for student: " + student.getStudentNumber());
        
        int totalCredits = student.getTotalCredits() != null ? student.getTotalCredits() : 0;
        int completedCredits = student.getCompletedCredits() != null ? student.getCompletedCredits() : 0;
        int failedCredits = student.getFailedCredits() != null ? student.getFailedCredits() : 0;
        
        // Vietnamese credit accumulation rules
        if (failedCredits > 12) {
            throw new RuntimeException("Too many failed credits (max 12). Current: " + failedCredits);
        }
        
        if (totalCredits > 200) {
            throw new RuntimeException("Total credits exceed maximum allowed (200). Current: " + totalCredits);
        }
        
        System.out.println("Credit accumulation validation passed");
    }
    
    /**
     * Validate graduation requirements
     */
    public void validateGraduationRequirements(Student student) {
        System.out.println("Validating graduation requirements for student: " + student.getStudentNumber());
        
        double gpa = student.getCurrentGpa() != null ? student.getCurrentGpa() : 0.0;
        int completedCredits = student.getCompletedCredits() != null ? student.getCompletedCredits() : 0;
        
        // Vietnamese graduation requirements
        if (!vietnameseIdService.isPassingGpa(gpa)) {
            throw new RuntimeException("GPA below graduation requirement (2.0). Current GPA: " + gpa);
        }
        
        if (completedCredits < 120) { // Typical requirement for 4-year program
            throw new RuntimeException("Insufficient credits for graduation. Completed: " + completedCredits + ", Required: 120");
        }
        
        System.out.println("Graduation requirements validation passed");
    }
    
    /**
     * Validate grade scale
     */
    public void validateGrade(double grade) {
        System.out.println("Validating grade: " + grade);
        
        if (grade < 0 || grade > 10) {
            throw new RuntimeException("Grade must be between 0 and 10. Provided: " + grade);
        }
        
        System.out.println("Grade validation passed");
    }
    
    /**
     * Validate attendance requirement
     */
    public void validateAttendance(double attendanceRate) {
        System.out.println("Validating attendance rate: " + attendanceRate + "%");
        
        if (attendanceRate < 75.0) {
            throw new RuntimeException("Attendance below minimum requirement (75%). Current: " + attendanceRate + "%");
        }
        
        System.out.println("Attendance validation passed");
    }
    
    /**
     * Validate academic level
     */
    public void validateAcademicLevel(String academicLevel) {
        System.out.println("Validating academic level: " + academicLevel);
        
        String[] validLevels = {"DAIHOC", "CAODANG", "THACSI", "TIENSI"};
        boolean isValid = false;
        
        for (String level : validLevels) {
            if (level.equals(academicLevel)) {
                isValid = true;
                break;
            }
        }
        
        if (!isValid) {
            throw new RuntimeException("Invalid academic level: " + academicLevel);
        }
        
        System.out.println("Academic level validation passed");
    }
    
    /**
     * Validate enrollment period
     */
    public void validateEnrollmentPeriod(Semester semester) {
        System.out.println("Validating enrollment period for semester: " + semester.getCode());
        
        // Check if enrollment period is open
        if (semester.getRegistrationStart() != null && semester.getRegistrationEnd() != null) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime start = semester.getRegistrationStart().atStartOfDay();
            java.time.LocalDateTime end = semester.getRegistrationEnd().atTime(23, 59, 59);
            
            if (now.isBefore(start) || now.isAfter(end)) {
                throw new RuntimeException("Enrollment period is not open for semester: " + semester.getCode());
            }
        }
        
        System.out.println("Enrollment period validation passed");
    }
    
    /**
     * Get maximum credits by academic level
     */
    private int getMaxCreditsByLevel(int enrollmentYear) {
        // Simplified logic based on enrollment year
        if (enrollmentYear <= 2) return 20;      // First 2 years
        if (enrollmentYear <= 4) return 25;      // Years 3-4
        return 30;                            // 5+ years (graduate)
    }
    
    /**
     * Get current credits for student in semester
     */
    private int getCurrentCredits(Student student, Semester semester) {
        // In production, this would query enrollments for the semester
        // For now, return a placeholder value
        return 15;
    }
    
    /**
     * Check if student has completed a course
     */
    private boolean hasCompletedCourse(Student student, String courseCode) {
        // In production, this would check student's transcript
        // For now, return false for demonstration
        return false;
    }
    
    /**
     * Check for schedule conflict
     */
    private boolean hasScheduleConflict(Student student, String newCourseSchedule) {
        // In production, this would check against student's current enrollments
        // For now, return false for demonstration
        return false;
    }
    
    /**
     * Validate Vietnamese name format
     */
    public void validateVietnameseName(String name) {
        System.out.println("Validating Vietnamese name: " + name);
        
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Vietnamese name cannot be empty");
        }
        
        if (name.length() < 2 || name.length() > 100) {
            throw new RuntimeException("Vietnamese name must be between 2 and 100 characters");
        }
        
        // Check for Vietnamese characters (basic validation)
        if (!containsVietnameseCharacters(name)) {
            System.out.println("Warning: Name may not contain Vietnamese characters");
        }
        
        System.out.println("Vietnamese name validation passed");
    }
    
    /**
     * Validate Vietnamese phone number format
     */
    public void validateVietnamesePhone(String phone) {
        System.out.println("Validating Vietnamese phone number: " + phone);
        
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("Vietnamese phone number cannot be empty");
        }
        
        // Vietnamese phone number format: 0XXXXXXXXX or +84XXXXXXXXX
        if (!phone.matches("^(0|\\+84)[0-9]{9,10}$")) {
            throw new RuntimeException("Invalid Vietnamese phone number format. Expected: 0XXXXXXXXX or +84XXXXXXXXX");
        }
        
        System.out.println("Vietnamese phone number validation passed");
    }
    
    /**
     * Validate Vietnamese email format
     */
    public void validateVietnameseEmail(String email) {
        System.out.println("Validating Vietnamese email: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Vietnamese email cannot be empty");
        }
        
        // Basic email validation
        if (!email.matches("^[A-Za-z0-9+._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Invalid email format");
        }
        
        // Check for Vietnamese educational domains
        if (!isVietnameseEducationalEmail(email)) {
            System.out.println("Warning: Email may not be from Vietnamese educational institution");
        }
        
        System.out.println("Vietnamese email validation passed");
    }
    
    /**
     * Check if string contains Vietnamese characters
     */
    private boolean containsVietnameseCharacters(String text) {
        // Basic check for Vietnamese characters
        return text.matches(".*[àáạảãâấầấẫẫăắằẳâă]*.*");
    }
    
    /**
     * Check if email is from Vietnamese educational institution
     */
    private boolean isVietnameseEducationalEmail(String email) {
        String[] vietnameseDomains = {
            "edu.vn",
            "vnu.edu.vn",
            "hcmut.edu.vn",
            "hust.edu.vn",
            "uet.edu.vn",
            "ptit.edu.vn",
            "bdu.edu.vn",
            "tlu.edu.vn",
            "ctu.edu.vn",
            "tdtu.edu.vn",
            "fpt.edu.vn",
            "rmit.edu.vn"
        };
        
        for (String domain : vietnameseDomains) {
            if (email.toLowerCase().endsWith(domain)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate Vietnamese ID number format
     */
    public void validateVietnameseIdNumber(String idNumber) {
        System.out.println("Validating Vietnamese ID number: " + idNumber);
        
        if (idNumber == null || idNumber.trim().isEmpty()) {
            throw new RuntimeException("Vietnamese ID number cannot be empty");
        }
        
        // Vietnamese ID number format: 9 or 12 digits
        if (!idNumber.matches("^[0-9]{9}$") && !idNumber.matches("^[0-9]{12}$")) {
            throw new RuntimeException("Invalid Vietnamese ID number format. Expected 9 or 12 digits");
        }
        
        System.out.println("Vietnamese ID number validation passed");
    }
    
    /**
     * Validate Vietnamese address format
     */
    public void validateVietnameseAddress(String address) {
        System.out.println("Validating Vietnamese address: " + address);
        
        if (address == null || address.trim().isEmpty()) {
            throw new RuntimeException("Vietnamese address cannot be empty");
        }
        
        if (address.length() < 10 || address.length() > 200) {
            throw new RuntimeException("Vietnamese address must be between 10 and 200 characters");
        }
        
        System.out.println("Vietnamese address validation passed");
    }
}
