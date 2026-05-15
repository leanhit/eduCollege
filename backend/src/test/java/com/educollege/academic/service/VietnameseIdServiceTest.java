package com.educollege.academic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class VietnameseIdServiceTest {

    @InjectMocks
    private VietnameseIdService vietnameseIdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValidStudentId() {
        // Valid 4-char faculty (13 chars)
        assertTrue(vietnameseIdService.isValidStudentId("SV24CNTT00101"));
        
        // Valid 3-char faculty (12 chars)
        assertTrue(vietnameseIdService.isValidStudentId("SV24TOA00050"));
        
        // Invalid lengths
        assertFalse(vietnameseIdService.isValidStudentId("SV24CNT01")); // Too short
        assertFalse(vietnameseIdService.isValidStudentId("SV24CNTT00101A")); // Too long
        
        // Invalid prefixes
        assertFalse(vietnameseIdService.isValidStudentId("AV24CNTT00101"));
        
        // Invalid sequence
        assertFalse(vietnameseIdService.isValidStudentId("SV24CNTTABCDE"));
    }

    @Test
    void testIsValidTeacherId() {
        // Valid teacher ID (10 chars)
        assertTrue(vietnameseIdService.isValidTeacherId("GVCNPM0001"));
        
        // Invalid length
        assertFalse(vietnameseIdService.isValidTeacherId("GVCNPM001"));
        assertFalse(vietnameseIdService.isValidTeacherId("GVCNPM00001"));
        
        // Invalid prefix
        assertFalse(vietnameseIdService.isValidTeacherId("TVCNPM0001"));
    }

    @Test
    void testExtractStudentIdInfo() {
        // Test with 4-char faculty
        VietnameseIdService.StudentIdInfo info4 = vietnameseIdService.extractStudentIdInfo("SV24CNTT00101");
        assertEquals(2024, info4.getYear());
        assertEquals("CNTT", info4.getFacultyCode());
        assertEquals(101, info4.getSequence());
        
        // Test with 3-char faculty
        VietnameseIdService.StudentIdInfo info3 = vietnameseIdService.extractStudentIdInfo("SV24TOA00050");
        assertEquals(2024, info3.getYear());
        assertEquals("TOA", info3.getFacultyCode());
        assertEquals(50, info3.getSequence());
    }

    @Test
    void testExtractTeacherIdInfo() {
        // Test with standard 4-char department (10 chars total)
        VietnameseIdService.TeacherIdInfo info = vietnameseIdService.extractTeacherIdInfo("GVCNPM0001");
        assertEquals("CNPM", info.getDepartmentCode());
        assertEquals(1, info.getSequence());
    }

    @Test
    void testCalculateGpa() {
        // Test Vietnamese 10-point to 4-point scale
        assertEquals(4.0, vietnameseIdService.calculateGpa(9.5));
        assertEquals(3.5, vietnameseIdService.calculateGpa(8.7));
        assertEquals(3.0, vietnameseIdService.calculateGpa(8.2));
        assertEquals(2.0, vietnameseIdService.calculateGpa(6.7));
        assertEquals(1.0, vietnameseIdService.calculateGpa(5.2));
        assertEquals(0.0, vietnameseIdService.calculateGpa(3.5));
        
        // Test invalid grade
        assertThrows(IllegalArgumentException.class, () -> vietnameseIdService.calculateGpa(11.0));
    }

    @Test
    void testGpaToLetterGrade() {
        assertEquals("A", vietnameseIdService.gpaToLetterGrade(3.9));
        assertEquals("B+", vietnameseIdService.gpaToLetterGrade(3.6));
        assertEquals("B", vietnameseIdService.gpaToLetterGrade(3.2));
        assertEquals("F", vietnameseIdService.gpaToLetterGrade(0.5));
    }

    @Test
    void testAcademicStanding() {
        // Should pass
        assertDoesNotThrow(() -> vietnameseIdService.validateAcademicStanding(3.0, 5));
        
        // Should fail due to low GPA
        Exception gpaEx = assertThrows(RuntimeException.class, () -> vietnameseIdService.validateAcademicStanding(1.5, 0));
        assertTrue(gpaEx.getMessage().contains("GPA below minimum"));
        
        // Should fail due to too many failed credits
        Exception creditEx = assertThrows(RuntimeException.class, () -> vietnameseIdService.validateAcademicStanding(2.5, 15));
        assertTrue(creditEx.getMessage().contains("Too many failed credits"));
    }

    @Test
    void testCourseLoad() {
        // Should pass for DAIHOC (max 25)
        assertDoesNotThrow(() -> vietnameseIdService.validateCourseLoad(20, "DAIHOC"));
        
        // Should fail for DAIHOC (exceed 25)
        assertThrows(RuntimeException.class, () -> vietnameseIdService.validateCourseLoad(26, "DAIHOC"));
        
        // Should fail for THACSI (max 18)
        assertThrows(RuntimeException.class, () -> vietnameseIdService.validateCourseLoad(20, "THACSI"));
    }
}
