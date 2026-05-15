package com.educollege.report.service;

import com.educollege.academic.model.Enrollment;
import com.educollege.academic.repository.EnrollmentRepository;
import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.repository.TuitionFeeRepository;
import com.educollege.report.dto.StudentTranscriptResponse;
import com.educollege.user.model.Student;
import com.educollege.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final TuitionFeeRepository tuitionFeeRepository;

    /**
     * Generate a full transcript for a student
     */
    public StudentTranscriptResponse getStudentTranscript(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        // Group enrollments by semester
        Map<String, List<Enrollment>> bySemester = enrollments.stream()
                .collect(Collectors.groupingBy(e -> e.getCourseOffering().getSemester().getName()));

        List<StudentTranscriptResponse.SemesterTranscript> semesterTranscripts = new ArrayList<>();
        double totalWeightedGpa = 0;
        int totalCredits = 0;

        for (Map.Entry<String, List<Enrollment>> entry : bySemester.entrySet()) {
            List<StudentTranscriptResponse.EnrollmentInfo> courses = entry.getValue().stream()
                    .map(e -> StudentTranscriptResponse.EnrollmentInfo.builder()
                            .courseCode(e.getCourseOffering().getCourse().getCode())
                            .courseName(e.getCourseOffering().getCourse().getName())
                            .credits(e.getCourseOffering().getCourse().getCredits())
                            .grade(e.getGrade())
                            .letterGrade(e.getLetterGrade())
                            .gpaPoints(e.getGpaPoints())
                            .build())
                    .collect(Collectors.toList());

            // Calculate Semester GPA
            double semesterWeightedGpa = entry.getValue().stream()
                    .filter(e -> e.getGpaPoints() != null)
                    .mapToDouble(e -> e.getGpaPoints() * e.getCourseOffering().getCourse().getCredits())
                    .sum();
            int semesterCredits = entry.getValue().stream()
                    .filter(e -> e.getGpaPoints() != null)
                    .mapToInt(e -> e.getCourseOffering().getCourse().getCredits())
                    .sum();
            
            double semesterGpa = semesterCredits > 0 ? semesterWeightedGpa / semesterCredits : 0;

            semesterTranscripts.add(StudentTranscriptResponse.SemesterTranscript.builder()
                    .semesterName(entry.getKey())
                    .courses(courses)
                    .semesterGpa(Math.round(semesterGpa * 100.0) / 100.0)
                    .build());

            totalWeightedGpa += semesterWeightedGpa;
            totalCredits += semesterCredits;
        }

        double cumulativeGpa = totalCredits > 0 ? totalWeightedGpa / totalCredits : 0;

        return StudentTranscriptResponse.builder()
                .studentName(student.getUser().getUsername()) // Should use fullName if available
                .studentId(student.getStudentNumber())
                .facultyName(student.getFaculty().getName())
                .className(student.getClassGroup().getName())
                .semesters(semesterTranscripts)
                .cumulativeGpa(Math.round(cumulativeGpa * 100.0) / 100.0)
                .totalCredits(totalCredits)
                .build();
    }

    /**
     * Get list of students with unpaid tuition fees for a semester
     */
    public List<TuitionFee> getTuitionDebtReport(Long semesterId) {
        return tuitionFeeRepository.findBySemesterId(semesterId).stream()
                .filter(tf -> tf.getTotalAmount().compareTo(tf.getPaidAmount()) > 0)
                .collect(Collectors.toList());
    }
}
