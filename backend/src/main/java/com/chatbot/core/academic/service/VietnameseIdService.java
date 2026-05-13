package com.chatbot.core.academic.service;

import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.model.Department;
import com.chatbot.core.academic.model.ClassGroup;
import com.chatbot.core.academic.repository.FacultyRepository;
import com.chatbot.core.academic.repository.DepartmentRepository;
import com.chatbot.core.academic.repository.ClassGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Vietnamese ID Service
 * Generates Vietnamese student and teacher IDs according to Vietnamese university standards
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VietnameseIdService {
    
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassGroupRepository classGroupRepository;
    
    // In-memory sequence counters (in production, use database)
    private final AtomicInteger studentSequence = new AtomicInteger(1);
    private final AtomicInteger teacherSequence = new AtomicInteger(1);
    
    /**
     * Generate Vietnamese Student ID
     * Format: SVYYFACULTYSEQUENCE
     * Example: SV24CNTT00101
     */
    public String generateStudentId(Faculty faculty, ClassGroup classGroup, Integer enrollmentYear) {
        System.out.println("Generating Vietnamese Student ID for faculty: " + faculty.getCode() + ", class: " + classGroup.getCode() + ", year: " + enrollmentYear);
        
        String yearFormat = String.format("%02d", enrollmentYear % 100); // YY format
        String facultyCode = faculty.getCode();
        String sequence = String.format("%05d", getNextStudentSequence(faculty, classGroup));
        
        String studentId = "SV" + yearFormat + facultyCode + sequence;
        System.out.println("Generated Student ID: " + studentId);
        return studentId;
    }
    
    /**
     * Generate Vietnamese Teacher ID
     * Format: GVDEPARTMENTSEQUENCE
     * Example: GVCNPM0001
     */
    public String generateTeacherId(Department department) {
        System.out.println("Generating Vietnamese Teacher ID for department: " + department.getCode());
        
        String departmentCode = department.getCode();
        String sequence = String.format("%04d", getNextTeacherSequence(department));
        
        String teacherId = "GV" + departmentCode + sequence;
        System.out.println("Generated Teacher ID: " + teacherId);
        return teacherId;
    }
    
    /**
     * Validate Vietnamese Student ID format
     * Pattern: ^SV[0-9]{2}[A-Z]{3}[0-9]{5}$
     */
    public boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.length() != 10) {
            return false;
        }
        return studentId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$");
    }
    
    /**
     * Validate Vietnamese Teacher ID format
     * Pattern: ^GV[A-Z]{4}[0-9]{4}$
     */
    public boolean isValidTeacherId(String teacherId) {
        if (teacherId == null || teacherId.length() != 8) {
            return false;
        }
        return teacherId.matches("^GV[A-Z]{4}[0-9]{4}$");
    }
    
    /**
     * Extract information from Vietnamese Student ID
     */
    public StudentIdInfo extractStudentIdInfo(String studentId) {
        if (!isValidStudentId(studentId)) {
            throw new IllegalArgumentException("Invalid Vietnamese Student ID format");
        }
        
        String yearStr = studentId.substring(2, 4);
        String facultyCode = studentId.substring(4, 7);
        String sequenceStr = studentId.substring(7, 12);
        
        int year = 2000 + Integer.parseInt(yearStr);
        int sequence = Integer.parseInt(sequenceStr);
        
        return StudentIdInfo.builder()
            .year(year)
            .facultyCode(facultyCode)
            .sequence(sequence)
            .build();
    }
    
    /**
     * Extract information from Vietnamese Teacher ID
     */
    public TeacherIdInfo extractTeacherIdInfo(String teacherId) {
        if (!isValidTeacherId(teacherId)) {
            throw new IllegalArgumentException("Invalid Vietnamese Teacher ID format");
        }
        
        String departmentCode = teacherId.substring(2, 6);
        String sequenceStr = teacherId.substring(6, 10);
        
        int sequence = Integer.parseInt(sequenceStr);
        
        return TeacherIdInfo.builder()
            .departmentCode(departmentCode)
            .sequence(sequence)
            .build();
    }
    
    /**
     * Get next student sequence for faculty and class
     */
    private int getNextStudentSequence(Faculty faculty, ClassGroup classGroup) {
        // In production, this should query database for the next sequence
        // For now, use in-memory counter
        return studentSequence.getAndIncrement();
    }
    
    /**
     * Get next teacher sequence for department
     */
    private int getNextTeacherSequence(Department department) {
        // In production, this should query database for the next sequence
        // For now, use in-memory counter
        return teacherSequence.getAndIncrement();
    }
    
    /**
     * Validate Vietnamese academic standing
     */
    public void validateAcademicStanding(double gpa, int failedCredits) {
        System.out.println("Validating academic standing: GPA=" + gpa + ", Failed Credits=" + failedCredits);
        
        // Vietnamese academic standing rules
        if (gpa < 2.0) {
            throw new RuntimeException("GPA below minimum requirement (2.0)");
        }
        
        if (failedCredits > 12) {
            throw new RuntimeException("Too many failed credits (max 12)");
        }
        
        System.out.println("Academic standing validation passed");
    }
    
    /**
     * Validate course load for student level
     */
    public void validateCourseLoad(int currentCredits, String academicLevel) {
        System.out.println("Validating course load: Current Credits=" + currentCredits + ", Level=" + academicLevel);
        
        int maxCredits = getMaxCreditsByLevel(academicLevel);
        
        if (currentCredits > maxCredits) {
            throw new RuntimeException("Course load exceeds maximum allowed (" + maxCredits + ")");
        }
        
        System.out.println("Course load validation passed");
    }
    
    /**
     * Get maximum credits by academic level
     */
    private int getMaxCreditsByLevel(String academicLevel) {
        switch (academicLevel) {
            case "DAIHOC":
                return 25;
            case "CAODANG":
                return 20;
            case "THACSI":
                return 18;
            case "TIENSI":
                return 15;
            default:
                return 25;
        }
    }
    
    /**
     * Check if student has prerequisites for course
     */
    public boolean hasPrerequisites(String studentId, String courseCode, String prerequisites) {
        System.out.println("Checking prerequisites for student: " + studentId + ", course: " + courseCode);
        
        if (prerequisites == null || prerequisites.trim().isEmpty()) {
            return true;
        }
        
        // In production, this would check student's completed courses
        // For now, return true for demonstration
        System.out.println("Prerequisites check completed");
        return true;
    }
    
    /**
     * Check for schedule conflicts
     */
    public boolean hasScheduleConflict(String studentId, String newCourseSchedule) {
        System.out.println("Checking schedule conflicts for student: " + studentId);
        
        // In production, this would check against student's current enrollments
        // For now, return false for demonstration
        System.out.println("Schedule conflict check completed");
        return false;
    }
    
    /**
     * Calculate GPA from Vietnamese grade scale
     */
    public double calculateGpa(double grade) {
        if (grade < 0 || grade > 10) {
            throw new IllegalArgumentException("Grade must be between 0 and 10");
        }
        
        // Vietnamese GPA scale (4.0 max)
        if (grade >= 9.0) return 4.0;      // A
        if (grade >= 8.5) return 3.5;      // B+
        if (grade >= 8.0) return 3.0;      // B
        if (grade >= 7.0) return 2.5;      // C+
        if (grade >= 6.5) return 2.0;      // C
        if (grade >= 5.5) return 1.5;      // D+
        if (grade >= 5.0) return 1.0;      // D
        return 0.0;                      // F
    }
    
    /**
     * Convert GPA to Vietnamese letter grade
     */
    public String gpaToLetterGrade(double gpa) {
        if (gpa >= 3.85) return "A";
        if (gpa >= 3.5) return "B+";
        if (gpa >= 3.0) return "B";
        if (gpa >= 2.5) return "C+";
        if (gpa >= 2.0) return "C";
        if (gpa >= 1.5) return "D+";
        if (gpa >= 1.0) return "D";
        return "F";
    }
    
    /**
     * Check if GPA is passing
     */
    public boolean isPassingGpa(double gpa) {
        return gpa >= 2.0;
    }
    
    /**
     * Check if GPA qualifies for honors
     */
    public boolean isHonorsGpa(double gpa) {
        return gpa >= 3.6;
    }
    
    /**
     * Student ID Information
     */
    public static class StudentIdInfo {
        private int year;
        private String facultyCode;
        private int sequence;
        
        public static StudentIdInfoBuilder builder() {
            return new StudentIdInfoBuilder();
        }
        
        // Getters and setters
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public String getFacultyCode() { return facultyCode; }
        public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }
        public int getSequence() { return sequence; }
        public void setSequence(int sequence) { this.sequence = sequence; }
        
        public static class StudentIdInfoBuilder {
            private int year;
            private String facultyCode;
            private int sequence;
            
            public StudentIdInfoBuilder year(int year) { this.year = year; return this; }
            public StudentIdInfoBuilder facultyCode(String facultyCode) { this.facultyCode = facultyCode; return this; }
            public StudentIdInfoBuilder sequence(int sequence) { this.sequence = sequence; return this; }
            
            public StudentIdInfo build() {
                StudentIdInfo info = new StudentIdInfo();
                info.year = this.year;
                info.facultyCode = this.facultyCode;
                info.sequence = this.sequence;
                return info;
            }
        }
    }
    
    /**
     * Teacher ID Information
     */
    public static class TeacherIdInfo {
        private String departmentCode;
        private int sequence;
        
        public static TeacherIdInfoBuilder builder() {
            return new TeacherIdInfoBuilder();
        }
        
        // Getters and setters
        public String getDepartmentCode() { return departmentCode; }
        public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
        public int getSequence() { return sequence; }
        public void setSequence(int sequence) { this.sequence = sequence; }
        
        public static class TeacherIdInfoBuilder {
            private String departmentCode;
            private int sequence;
            
            public TeacherIdInfoBuilder departmentCode(String departmentCode) { this.departmentCode = departmentCode; return this; }
            public TeacherIdInfoBuilder sequence(int sequence) { this.sequence = sequence; return this; }
            
            public TeacherIdInfo build() {
                TeacherIdInfo info = new TeacherIdInfo();
                info.departmentCode = this.departmentCode;
                info.sequence = this.sequence;
                return info;
            }
        }
    }
}
