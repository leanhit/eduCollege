package com.educollege.report.dto;

import com.educollege.academic.model.Enrollment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentTranscriptResponse {
    private String studentName;
    private String studentId;
    private String facultyName;
    private String className;
    private List<SemesterTranscript> semesters;
    private Double cumulativeGpa;
    private Integer totalCredits;

    @Data
    @Builder
    public static class SemesterTranscript {
        private String semesterName;
        private List<EnrollmentInfo> courses;
        private Double semesterGpa;
    }

    @Data
    @Builder
    public static class EnrollmentInfo {
        private String courseCode;
        private String courseName;
        private Integer credits;
        private Double grade;
        private String letterGrade;
        private Double gpaPoints;
    }
}
