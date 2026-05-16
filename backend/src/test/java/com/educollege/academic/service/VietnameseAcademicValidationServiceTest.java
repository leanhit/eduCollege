package com.educollege.academic.service;

import com.educollege.user.model.Student;
import com.educollege.academic.model.Semester;
import com.educollege.user.repository.StudentRepository;
import com.educollege.academic.repository.SemesterRepository;
import com.educollege.academic.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class VietnameseAcademicValidationServiceTest {

    @InjectMocks
    private VietnameseAcademicValidationService validationService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private VietnameseIdService vietnameseIdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateStudentId() {
        // Valid student IDs
        assertDoesNotThrow(() -> validationService.validateStudentId("SV24CNTT00101"));
        assertDoesNotThrow(() -> validationService.validateStudentId("SV24TOA00050"));

        // Invalid student IDs
        assertThrows(RuntimeException.class, () -> validationService.validateStudentId("SV24CNT01"));
        assertThrows(RuntimeException.class, () -> validationService.validateStudentId("AV24CNTT00101"));
    }

    @Test
    void testValidateTeacherId() {
        // Valid teacher ID
        assertDoesNotThrow(() -> validationService.validateTeacherId("GVCNPM0001"));

        // Invalid teacher IDs
        assertThrows(RuntimeException.class, () -> validationService.validateTeacherId("GVCNPM001"));
        assertThrows(RuntimeException.class, () -> validationService.validateTeacherId("TVCNPM0001"));
    }

    @Test
    void testValidateAcademicStanding() {
        Student student = new Student();
        student.setStudentNumber("SV24CNTT00101");
        student.setCurrentGpa(3.5);
        student.setFailedCredits(5);

        // Should pass
        assertDoesNotThrow(() -> validationService.validateAcademicStanding(student));

        // Should fail due to low GPA
        student.setCurrentGpa(1.5);
        Exception gpaEx = assertThrows(RuntimeException.class, () -> validationService.validateAcademicStanding(student));
        assertTrue(gpaEx.getMessage().contains("GPA below minimum"));

        // Should fail due to too many failed credits
        student.setCurrentGpa(3.0);
        student.setFailedCredits(15);
        Exception creditEx = assertThrows(RuntimeException.class, () -> validationService.validateAcademicStanding(student));
        assertTrue(creditEx.getMessage().contains("Too many failed credits"));
    }

    @Test
    void testValidateCourseLoad() {
        Student student = new Student();
        student.setStudentNumber("SV24CNTT00101");
        student.setEnrollmentYear(2024);

        Semester semester = new Semester();
        semester.setId(1L);
        semester.setCode("20241");

        when(enrollmentRepository.findByStudentIdAndSemesterId(anyLong(), anyLong())).thenReturn(java.util.List.of());

        // Should pass for first year (max 20)
        assertDoesNotThrow(() -> validationService.validateCourseLoad(student, semester));

        // Should fail for first year (exceed 20)
        when(enrollmentRepository.findByStudentIdAndSemesterId(anyLong(), anyLong()))
            .thenReturn(java.util.List.of(
                createMockEnrollment(10),
                createMockEnrollment(11)
            ));
        assertThrows(RuntimeException.class, () -> validationService.validateCourseLoad(student, semester));
    }

    @Test
    void testValidatePrerequisites() {
        Student student = new Student();
        student.setStudentNumber("SV24CNTT00101");

        // No prerequisites - should pass
        assertDoesNotThrow(() -> validationService.validatePrerequisites(student, "TIN102", ""));

        // With prerequisites - should fail (student hasn't completed)
        assertThrows(RuntimeException.class, () -> 
            validationService.validatePrerequisites(student, "TIN102", "TIN101"));
    }

    @Test
    void testValidateEnrollmentCapacity() {
        // Should pass
        assertDoesNotThrow(() -> validationService.validateEnrollmentCapacity(40, 50));

        // Should fail - course is full
        Exception ex = assertThrows(RuntimeException.class, () -> 
            validationService.validateEnrollmentCapacity(50, 50));
        assertTrue(ex.getMessage().contains("Course is full"));
    }

    @Test
    void testValidateGrade() {
        // Valid grades
        assertDoesNotThrow(() -> validationService.validateGrade(8.5));
        assertDoesNotThrow(() -> validationService.validateGrade(0.0));
        assertDoesNotThrow(() -> validationService.validateGrade(10.0));

        // Invalid grades
        assertThrows(RuntimeException.class, () -> validationService.validateGrade(-1.0));
        assertThrows(RuntimeException.class, () -> validationService.validateGrade(11.0));
    }

    @Test
    void testValidateAttendance() {
        // Valid attendance
        assertDoesNotThrow(() -> validationService.validateAttendance(75.0));
        assertDoesNotThrow(() -> validationService.validateAttendance(100.0));

        // Invalid attendance
        Exception ex = assertThrows(RuntimeException.class, () -> validationService.validateAttendance(70.0));
        assertTrue(ex.getMessage().contains("Attendance below minimum"));
    }

    @Test
    void testValidateAcademicLevel() {
        // Valid levels
        assertDoesNotThrow(() -> validationService.validateAcademicLevel("DAIHOC"));
        assertDoesNotThrow(() -> validationService.validateAcademicLevel("CAODANG"));
        assertDoesNotThrow(() -> validationService.validateAcademicLevel("THACSI"));
        assertDoesNotThrow(() -> validationService.validateAcademicLevel("TIENSI"));

        // Invalid level
        assertThrows(RuntimeException.class, () -> validationService.validateAcademicLevel("INVALID"));
    }

    @Test
    void testValidateVietnameseName() {
        // Valid names
        assertDoesNotThrow(() -> validationService.validateVietnameseName("Nguyễn Văn A"));
        assertDoesNotThrow(() -> validationService.validateVietnameseName("Trần Thị B"));

        // Invalid names
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseName(""));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseName("A"));
    }

    @Test
    void testValidateVietnamesePhone() {
        // Valid phone numbers
        assertDoesNotThrow(() -> validationService.validateVietnamesePhone("0912345678"));
        assertDoesNotThrow(() -> validationService.validateVietnamesePhone("+84912345678"));

        // Invalid phone numbers
        assertThrows(RuntimeException.class, () -> validationService.validateVietnamesePhone(""));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnamesePhone("123456"));
    }

    @Test
    void testValidateVietnameseEmail() {
        // Valid emails
        assertDoesNotThrow(() -> validationService.validateVietnameseEmail("student@educollege.edu.vn"));
        assertDoesNotThrow(() -> validationService.validateVietnameseEmail("user@gmail.com"));

        // Invalid emails
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseEmail(""));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseEmail("invalid-email"));
    }

    @Test
    void testValidateVietnameseIdNumber() {
        // Valid ID numbers (9 or 12 digits)
        assertDoesNotThrow(() -> validationService.validateVietnameseIdNumber("123456789"));
        assertDoesNotThrow(() -> validationService.validateVietnameseIdNumber("123456789012"));

        // Invalid ID numbers
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseIdNumber(""));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseIdNumber("12345678"));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseIdNumber("1234567890123"));
    }

    @Test
    void testValidateVietnameseAddress() {
        // Valid addresses
        assertDoesNotThrow(() -> validationService.validateVietnameseAddress("123 Đường ABC, Quận 1, TP.HCM"));
        assertDoesNotThrow(() -> validationService.validateVietnameseAddress("456 XYZ, Hà Nội"));

        // Invalid addresses
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseAddress(""));
        assertThrows(RuntimeException.class, () -> validationService.validateVietnameseAddress("ABC"));
    }

    @Test
    void testValidateGraduationRequirements() {
        Student student = new Student();
        student.setStudentNumber("SV24CNTT00101");
        student.setCurrentGpa(3.5);
        student.setCompletedCredits(120);

        // Should pass
        assertDoesNotThrow(() -> validationService.validateGraduationRequirements(student));

        // Should fail due to low GPA
        student.setCurrentGpa(1.5);
        Exception gpaEx = assertThrows(RuntimeException.class, () -> validationService.validateGraduationRequirements(student));
        assertTrue(gpaEx.getMessage().contains("GPA below graduation"));

        // Should fail due to insufficient credits
        student.setCurrentGpa(3.0);
        student.setCompletedCredits(100);
        Exception creditEx = assertThrows(RuntimeException.class, () -> validationService.validateGraduationRequirements(student));
        assertTrue(creditEx.getMessage().contains("Insufficient credits"));
    }

    @Test
    void testValidateCreditAccumulation() {
        Student student = new Student();
        student.setStudentNumber("SV24CNTT00101");
        student.setTotalCredits(150);
        student.setCompletedCredits(120);
        student.setFailedCredits(5);

        // Should pass
        assertDoesNotThrow(() -> validationService.validateCreditAccumulation(student));

        // Should fail due to too many failed credits
        student.setFailedCredits(15);
        Exception failedEx = assertThrows(RuntimeException.class, () -> validationService.validateCreditAccumulation(student));
        assertTrue(failedEx.getMessage().contains("Too many failed credits"));

        // Should fail due to too many total credits
        student.setFailedCredits(5);
        student.setTotalCredits(250);
        Exception totalEx = assertThrows(RuntimeException.class, () -> validationService.validateCreditAccumulation(student));
        assertTrue(totalEx.getMessage().contains("Total credits exceed"));
    }

    private com.educollege.academic.model.Enrollment createMockEnrollment(int credits) {
        com.educollege.academic.model.Enrollment enrollment = new com.educollege.academic.model.Enrollment();
        com.educollege.academic.model.CourseOffering offering = new com.educollege.academic.model.CourseOffering();
        com.educollege.academic.model.Course course = new com.educollege.academic.model.Course();
        course.setCredits(credits);
        offering.setCourse(course);
        enrollment.setCourseOffering(offering);
        return enrollment;
    }
}
