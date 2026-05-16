package com.educollege.report.service;

import com.educollege.academic.model.Faculty;
import com.educollege.academic.model.Course;
import com.educollege.academic.model.Enrollment;
import com.educollege.user.model.Student;
import com.educollege.academic.repository.FacultyRepository;
import com.educollege.academic.repository.CourseRepository;
import com.educollege.academic.repository.EnrollmentRepository;
import com.educollege.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced Reporting Service
 * Generates comprehensive academic reports according to Vietnamese university standards
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdvancedReportingService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * Generate student academic summary report
     */
    public StudentAcademicSummary generateStudentSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        double totalGpa = enrollments.stream()
            .filter(e -> e.getGpaPoints() != null)
            .mapToDouble(Enrollment::getGpaPoints)
            .average()
            .orElse(0.0);

        int totalCredits = enrollments.stream()
            .mapToInt(e -> e.getCourseOffering().getCourse().getCredits())
            .sum();

        int completedCredits = enrollments.stream()
            .filter(e -> e.getStatus() == com.educollege.core.enums.EnrollmentStatus.COMPLETED)
            .mapToInt(e -> e.getCourseOffering().getCourse().getCredits())
            .sum();

        int failedCredits = enrollments.stream()
            .filter(e -> e.getStatus() == com.educollege.core.enums.EnrollmentStatus.FAILED)
            .mapToInt(e -> e.getCourseOffering().getCourse().getCredits())
            .sum();

        return StudentAcademicSummary.builder()
            .studentId(student.getId())
            .vietnameseId(student.getStudentNumber())
            .fullName(student.getUser().getVietnameseId())
            .facultyCode(student.getFaculty().getCode())
            .facultyName(student.getFaculty().getVietnameseName())
            .classCode(student.getClassGroup() != null ? student.getClassGroup().getCode() : "N/A")
            .currentGpa(totalGpa)
            .cumulativeGpa(student.getCumulativeGpa())
            .totalCredits(totalCredits)
            .completedCredits(completedCredits)
            .failedCredits(failedCredits)
            .academicStanding(student.getAcademicStanding())
            .studentStatus(student.getStudentStatus().name())
            .enrollmentYear(student.getEnrollmentYear())
            .graduationYear(student.getGraduationYear())
            .generatedAt(LocalDateTime.now())
            .build();
    }

    /**
     * Generate faculty workload report
     */
    public FacultyWorkloadReport generateFacultyWorkloadReport(Long facultyId, Integer academicYear) {
        Faculty faculty = facultyRepository.findById(facultyId)
            .orElseThrow(() -> new RuntimeException("Faculty not found"));

        List<Student> students = studentRepository.findByFacultyId(facultyId);

        int totalStudents = students.size();
        int enrolledStudents = (int) students.stream()
            .filter(s -> s.getStudentStatus() == com.educollege.core.enums.StudentStatus.ENROLLED)
            .count();

        int graduatedStudents = (int) students.stream()
            .filter(s -> s.getStudentStatus() == com.educollege.core.enums.StudentStatus.GRADUATED)
            .count();

        double averageGpa = students.stream()
            .filter(s -> s.getCurrentGpa() != null)
            .mapToDouble(Student::getCurrentGpa)
            .average()
            .orElse(0.0);

        return FacultyWorkloadReport.builder()
            .facultyId(faculty.getId())
            .facultyCode(faculty.getCode())
            .facultyName(faculty.getVietnameseName())
            .academicYear(academicYear)
            .totalStudents(totalStudents)
            .enrolledStudents(enrolledStudents)
            .graduatedStudents(graduatedStudents)
            .averageGpa(averageGpa)
            .generatedAt(LocalDateTime.now())
            .build();
    }

    /**
     * Generate course enrollment statistics
     */
    public CourseEnrollmentStatistics generateCourseStatistics(Long courseId, Long semesterId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Enrollment> enrollments = enrollmentRepository.findAll().stream()
            .filter(e -> e.getCourseOffering().getCourse().getId().equals(courseId))
            .toList();

        int totalEnrollments = enrollments.size();
        int completedEnrollments = (int) enrollments.stream()
            .filter(e -> e.getStatus() == com.educollege.core.enums.EnrollmentStatus.COMPLETED)
            .count();

        int failedEnrollments = (int) enrollments.stream()
            .filter(e -> e.getStatus() == com.educollege.core.enums.EnrollmentStatus.FAILED)
            .count();

        double averageGrade = enrollments.stream()
            .filter(e -> e.getGrade() != null)
            .mapToDouble(Enrollment::getGrade)
            .average()
            .orElse(0.0);

        Map<String, Long> gradeDistribution = enrollments.stream()
            .filter(e -> e.getLetterGrade() != null)
            .collect(Collectors.groupingBy(Enrollment::getLetterGrade, Collectors.counting()));

        return CourseEnrollmentStatistics.builder()
            .courseId(course.getId())
            .courseCode(course.getCode())
            .courseName(course.getVietnameseName())
            .credits(course.getCredits())
            .courseType(course.getCourseType().name())
            .totalEnrollments(totalEnrollments)
            .completedEnrollments(completedEnrollments)
            .failedEnrollments(failedEnrollments)
            .averageGrade(averageGrade)
            .gradeDistribution(gradeDistribution)
            .generatedAt(LocalDateTime.now())
            .build();
    }

    /**
     * Generate graduation eligibility report
     */
    public GraduationEligibilityReport generateGraduationEligibilityReport(Long facultyId, Integer academicYear) {
        List<Student> students = studentRepository.findByFacultyId(facultyId);

        int totalStudents = students.size();
        int eligibleStudents = 0;
        int notEligibleStudents = 0;

        List<GraduationEligibilityDetail> details = new ArrayList<>();

        for (Student student : students) {
            boolean eligible = isEligibleForGraduation(student);
            if (eligible) {
                eligibleStudents++;
            } else {
                notEligibleStudents++;
            }

            details.add(GraduationEligibilityDetail.builder()
                .studentId(student.getId())
                .vietnameseId(student.getStudentNumber())
                .currentGpa(student.getCurrentGpa())
                .completedCredits(student.getCompletedCredits())
                .failedCredits(student.getFailedCredits())
                .eligible(eligible)
                .reason(getGraduationIneligibilityReason(student))
                .build());
        }

        return GraduationEligibilityReport.builder()
            .facultyId(facultyId)
            .academicYear(academicYear)
            .totalStudents(totalStudents)
            .eligibleStudents(eligibleStudents)
            .notEligibleStudents(notEligibleStudents)
            .eligibilityRate(totalStudents > 0 ? (double) eligibleStudents / totalStudents * 100 : 0.0)
            .details(details)
            .generatedAt(LocalDateTime.now())
            .build();
    }

    /**
     * Generate academic performance trend report
     */
    public AcademicPerformanceTrend generatePerformanceTrend(Long facultyId, Integer startYear, Integer endYear) {
        Map<Integer, YearlyPerformance> yearlyData = new TreeMap<>();

        for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
            final int year = currentYear;
            List<Student> students = studentRepository.findByFacultyId(facultyId).stream()
                .filter(s -> s.getEnrollmentYear() != null && s.getEnrollmentYear().equals(year))
                .toList();

            double averageGpa = students.stream()
                .filter(s -> s.getCurrentGpa() != null)
                .mapToDouble(Student::getCurrentGpa)
                .average()
                .orElse(0.0);

            int totalStudents = students.size();
            int goodStanding = (int) students.stream()
                .filter(s -> "GOOD".equals(s.getAcademicStanding()))
                .count();

            int probation = (int) students.stream()
                .filter(s -> "PROBATION".equals(s.getAcademicStanding()))
                .count();

            yearlyData.put(year, YearlyPerformance.builder()
                .year(year)
                .totalStudents(totalStudents)
                .averageGpa(averageGpa)
                .goodStanding(goodStanding)
                .probation(probation)
                .build());
        }

        return AcademicPerformanceTrend.builder()
            .facultyId(facultyId)
            .startYear(startYear)
            .endYear(endYear)
            .yearlyData(yearlyData)
            .generatedAt(LocalDateTime.now())
            .build();
    }

    private boolean isEligibleForGraduation(Student student) {
        if (student.getCurrentGpa() == null || student.getCurrentGpa() < 2.0) {
            return false;
        }
        if (student.getCompletedCredits() == null || student.getCompletedCredits() < 120) {
            return false;
        }
        if (student.getFailedCredits() != null && student.getFailedCredits() > 12) {
            return false;
        }
        return true;
    }

    private String getGraduationIneligibilityReason(Student student) {
        if (student.getCurrentGpa() == null || student.getCurrentGpa() < 2.0) {
            return "GPA below minimum requirement (2.0)";
        }
        if (student.getCompletedCredits() == null || student.getCompletedCredits() < 120) {
            return "Insufficient credits for graduation (minimum 120)";
        }
        if (student.getFailedCredits() != null && student.getFailedCredits() > 12) {
            return "Too many failed credits (maximum 12)";
        }
        return "Unknown reason";
    }

    // DTO classes for reports

    public static class StudentAcademicSummary {
        private Long studentId;
        private String vietnameseId;
        private String fullName;
        private String facultyCode;
        private String facultyName;
        private String classCode;
        private Double currentGpa;
        private Double cumulativeGpa;
        private Integer totalCredits;
        private Integer completedCredits;
        private Integer failedCredits;
        private String academicStanding;
        private String studentStatus;
        private Integer enrollmentYear;
        private Integer graduationYear;
        private LocalDateTime generatedAt;

        public static StudentAcademicSummaryBuilder builder() {
            return new StudentAcademicSummaryBuilder();
        }

        // Getters and setters
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getVietnameseId() { return vietnameseId; }
        public void setVietnameseId(String vietnameseId) { this.vietnameseId = vietnameseId; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getFacultyCode() { return facultyCode; }
        public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public String getClassCode() { return classCode; }
        public void setClassCode(String classCode) { this.classCode = classCode; }
        public Double getCurrentGpa() { return currentGpa; }
        public void setCurrentGpa(Double currentGpa) { this.currentGpa = currentGpa; }
        public Double getCumulativeGpa() { return cumulativeGpa; }
        public void setCumulativeGpa(Double cumulativeGpa) { this.cumulativeGpa = cumulativeGpa; }
        public Integer getTotalCredits() { return totalCredits; }
        public void setTotalCredits(Integer totalCredits) { this.totalCredits = totalCredits; }
        public Integer getCompletedCredits() { return completedCredits; }
        public void setCompletedCredits(Integer completedCredits) { this.completedCredits = completedCredits; }
        public Integer getFailedCredits() { return failedCredits; }
        public void setFailedCredits(Integer failedCredits) { this.failedCredits = failedCredits; }
        public String getAcademicStanding() { return academicStanding; }
        public void setAcademicStanding(String academicStanding) { this.academicStanding = academicStanding; }
        public String getStudentStatus() { return studentStatus; }
        public void setStudentStatus(String studentStatus) { this.studentStatus = studentStatus; }
        public Integer getEnrollmentYear() { return enrollmentYear; }
        public void setEnrollmentYear(Integer enrollmentYear) { this.enrollmentYear = enrollmentYear; }
        public Integer getGraduationYear() { return graduationYear; }
        public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static class StudentAcademicSummaryBuilder {
            private StudentAcademicSummary summary = new StudentAcademicSummary();
            public StudentAcademicSummaryBuilder studentId(Long studentId) { summary.studentId = studentId; return this; }
            public StudentAcademicSummaryBuilder vietnameseId(String vietnameseId) { summary.vietnameseId = vietnameseId; return this; }
            public StudentAcademicSummaryBuilder fullName(String fullName) { summary.fullName = fullName; return this; }
            public StudentAcademicSummaryBuilder facultyCode(String facultyCode) { summary.facultyCode = facultyCode; return this; }
            public StudentAcademicSummaryBuilder facultyName(String facultyName) { summary.facultyName = facultyName; return this; }
            public StudentAcademicSummaryBuilder classCode(String classCode) { summary.classCode = classCode; return this; }
            public StudentAcademicSummaryBuilder currentGpa(Double currentGpa) { summary.currentGpa = currentGpa; return this; }
            public StudentAcademicSummaryBuilder cumulativeGpa(Double cumulativeGpa) { summary.cumulativeGpa = cumulativeGpa; return this; }
            public StudentAcademicSummaryBuilder totalCredits(Integer totalCredits) { summary.totalCredits = totalCredits; return this; }
            public StudentAcademicSummaryBuilder completedCredits(Integer completedCredits) { summary.completedCredits = completedCredits; return this; }
            public StudentAcademicSummaryBuilder failedCredits(Integer failedCredits) { summary.failedCredits = failedCredits; return this; }
            public StudentAcademicSummaryBuilder academicStanding(String academicStanding) { summary.academicStanding = academicStanding; return this; }
            public StudentAcademicSummaryBuilder studentStatus(String studentStatus) { summary.studentStatus = studentStatus; return this; }
            public StudentAcademicSummaryBuilder enrollmentYear(Integer enrollmentYear) { summary.enrollmentYear = enrollmentYear; return this; }
            public StudentAcademicSummaryBuilder graduationYear(Integer graduationYear) { summary.graduationYear = graduationYear; return this; }
            public StudentAcademicSummaryBuilder generatedAt(LocalDateTime generatedAt) { summary.generatedAt = generatedAt; return this; }
            public StudentAcademicSummary build() { return summary; }
        }
    }

    public static class FacultyWorkloadReport {
        private Long facultyId;
        private String facultyCode;
        private String facultyName;
        private Integer academicYear;
        private Integer totalStudents;
        private Integer enrolledStudents;
        private Integer graduatedStudents;
        private Double averageGpa;
        private LocalDateTime generatedAt;

        public static FacultyWorkloadReportBuilder builder() {
            return new FacultyWorkloadReportBuilder();
        }

        // Getters and setters
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public String getFacultyCode() { return facultyCode; }
        public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public Integer getAcademicYear() { return academicYear; }
        public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Integer getEnrolledStudents() { return enrolledStudents; }
        public void setEnrolledStudents(Integer enrolledStudents) { this.enrolledStudents = enrolledStudents; }
        public Integer getGraduatedStudents() { return graduatedStudents; }
        public void setGraduatedStudents(Integer graduatedStudents) { this.graduatedStudents = graduatedStudents; }
        public Double getAverageGpa() { return averageGpa; }
        public void setAverageGpa(Double averageGpa) { this.averageGpa = averageGpa; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static class FacultyWorkloadReportBuilder {
            private FacultyWorkloadReport report = new FacultyWorkloadReport();
            public FacultyWorkloadReportBuilder facultyId(Long facultyId) { report.facultyId = facultyId; return this; }
            public FacultyWorkloadReportBuilder facultyCode(String facultyCode) { report.facultyCode = facultyCode; return this; }
            public FacultyWorkloadReportBuilder facultyName(String facultyName) { report.facultyName = facultyName; return this; }
            public FacultyWorkloadReportBuilder academicYear(Integer academicYear) { report.academicYear = academicYear; return this; }
            public FacultyWorkloadReportBuilder totalStudents(Integer totalStudents) { report.totalStudents = totalStudents; return this; }
            public FacultyWorkloadReportBuilder enrolledStudents(Integer enrolledStudents) { report.enrolledStudents = enrolledStudents; return this; }
            public FacultyWorkloadReportBuilder graduatedStudents(Integer graduatedStudents) { report.graduatedStudents = graduatedStudents; return this; }
            public FacultyWorkloadReportBuilder averageGpa(Double averageGpa) { report.averageGpa = averageGpa; return this; }
            public FacultyWorkloadReportBuilder generatedAt(LocalDateTime generatedAt) { report.generatedAt = generatedAt; return this; }
            public FacultyWorkloadReport build() { return report; }
        }
    }

    public static class CourseEnrollmentStatistics {
        private Long courseId;
        private String courseCode;
        private String courseName;
        private Integer credits;
        private String courseType;
        private Integer totalEnrollments;
        private Integer completedEnrollments;
        private Integer failedEnrollments;
        private Double averageGrade;
        private Map<String, Long> gradeDistribution;
        private LocalDateTime generatedAt;

        public static CourseEnrollmentStatisticsBuilder builder() {
            return new CourseEnrollmentStatisticsBuilder();
        }

        // Getters and setters
        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }
        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }
        public String getCourseType() { return courseType; }
        public void setCourseType(String courseType) { this.courseType = courseType; }
        public Integer getTotalEnrollments() { return totalEnrollments; }
        public void setTotalEnrollments(Integer totalEnrollments) { this.totalEnrollments = totalEnrollments; }
        public Integer getCompletedEnrollments() { return completedEnrollments; }
        public void setCompletedEnrollments(Integer completedEnrollments) { this.completedEnrollments = completedEnrollments; }
        public Integer getFailedEnrollments() { return failedEnrollments; }
        public void setFailedEnrollments(Integer failedEnrollments) { this.failedEnrollments = failedEnrollments; }
        public Double getAverageGrade() { return averageGrade; }
        public void setAverageGrade(Double averageGrade) { this.averageGrade = averageGrade; }
        public Map<String, Long> getGradeDistribution() { return gradeDistribution; }
        public void setGradeDistribution(Map<String, Long> gradeDistribution) { this.gradeDistribution = gradeDistribution; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static class CourseEnrollmentStatisticsBuilder {
            private CourseEnrollmentStatistics stats = new CourseEnrollmentStatistics();
            public CourseEnrollmentStatisticsBuilder courseId(Long courseId) { stats.courseId = courseId; return this; }
            public CourseEnrollmentStatisticsBuilder courseCode(String courseCode) { stats.courseCode = courseCode; return this; }
            public CourseEnrollmentStatisticsBuilder courseName(String courseName) { stats.courseName = courseName; return this; }
            public CourseEnrollmentStatisticsBuilder credits(Integer credits) { stats.credits = credits; return this; }
            public CourseEnrollmentStatisticsBuilder courseType(String courseType) { stats.courseType = courseType; return this; }
            public CourseEnrollmentStatisticsBuilder totalEnrollments(Integer totalEnrollments) { stats.totalEnrollments = totalEnrollments; return this; }
            public CourseEnrollmentStatisticsBuilder completedEnrollments(Integer completedEnrollments) { stats.completedEnrollments = completedEnrollments; return this; }
            public CourseEnrollmentStatisticsBuilder failedEnrollments(Integer failedEnrollments) { stats.failedEnrollments = failedEnrollments; return this; }
            public CourseEnrollmentStatisticsBuilder averageGrade(Double averageGrade) { stats.averageGrade = averageGrade; return this; }
            public CourseEnrollmentStatisticsBuilder gradeDistribution(Map<String, Long> gradeDistribution) { stats.gradeDistribution = gradeDistribution; return this; }
            public CourseEnrollmentStatisticsBuilder generatedAt(LocalDateTime generatedAt) { stats.generatedAt = generatedAt; return this; }
            public CourseEnrollmentStatistics build() { return stats; }
        }
    }

    public static class GraduationEligibilityReport {
        private Long facultyId;
        private Integer academicYear;
        private Integer totalStudents;
        private Integer eligibleStudents;
        private Integer notEligibleStudents;
        private Double eligibilityRate;
        private List<GraduationEligibilityDetail> details;
        private LocalDateTime generatedAt;

        public static GraduationEligibilityReportBuilder builder() {
            return new GraduationEligibilityReportBuilder();
        }

        // Getters and setters
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public Integer getAcademicYear() { return academicYear; }
        public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Integer getEligibleStudents() { return eligibleStudents; }
        public void setEligibleStudents(Integer eligibleStudents) { this.eligibleStudents = eligibleStudents; }
        public Integer getNotEligibleStudents() { return notEligibleStudents; }
        public void setNotEligibleStudents(Integer notEligibleStudents) { this.notEligibleStudents = notEligibleStudents; }
        public Double getEligibilityRate() { return eligibilityRate; }
        public void setEligibilityRate(Double eligibilityRate) { this.eligibilityRate = eligibilityRate; }
        public List<GraduationEligibilityDetail> getDetails() { return details; }
        public void setDetails(List<GraduationEligibilityDetail> details) { this.details = details; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static class GraduationEligibilityReportBuilder {
            private GraduationEligibilityReport report = new GraduationEligibilityReport();
            public GraduationEligibilityReportBuilder facultyId(Long facultyId) { report.facultyId = facultyId; return this; }
            public GraduationEligibilityReportBuilder academicYear(Integer academicYear) { report.academicYear = academicYear; return this; }
            public GraduationEligibilityReportBuilder totalStudents(Integer totalStudents) { report.totalStudents = totalStudents; return this; }
            public GraduationEligibilityReportBuilder eligibleStudents(Integer eligibleStudents) { report.eligibleStudents = eligibleStudents; return this; }
            public GraduationEligibilityReportBuilder notEligibleStudents(Integer notEligibleStudents) { report.notEligibleStudents = notEligibleStudents; return this; }
            public GraduationEligibilityReportBuilder eligibilityRate(Double eligibilityRate) { report.eligibilityRate = eligibilityRate; return this; }
            public GraduationEligibilityReportBuilder details(List<GraduationEligibilityDetail> details) { report.details = details; return this; }
            public GraduationEligibilityReportBuilder generatedAt(LocalDateTime generatedAt) { report.generatedAt = generatedAt; return this; }
            public GraduationEligibilityReport build() { return report; }
        }
    }

    public static class GraduationEligibilityDetail {
        private Long studentId;
        private String vietnameseId;
        private Double currentGpa;
        private Integer completedCredits;
        private Integer failedCredits;
        private Boolean eligible;
        private String reason;

        public static GraduationEligibilityDetailBuilder builder() {
            return new GraduationEligibilityDetailBuilder();
        }

        // Getters and setters
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getVietnameseId() { return vietnameseId; }
        public void setVietnameseId(String vietnameseId) { this.vietnameseId = vietnameseId; }
        public Double getCurrentGpa() { return currentGpa; }
        public void setCurrentGpa(Double currentGpa) { this.currentGpa = currentGpa; }
        public Integer getCompletedCredits() { return completedCredits; }
        public void setCompletedCredits(Integer completedCredits) { this.completedCredits = completedCredits; }
        public Integer getFailedCredits() { return failedCredits; }
        public void setFailedCredits(Integer failedCredits) { this.failedCredits = failedCredits; }
        public Boolean getEligible() { return eligible; }
        public void setEligible(Boolean eligible) { this.eligible = eligible; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public static class GraduationEligibilityDetailBuilder {
            private GraduationEligibilityDetail detail = new GraduationEligibilityDetail();
            public GraduationEligibilityDetailBuilder studentId(Long studentId) { detail.studentId = studentId; return this; }
            public GraduationEligibilityDetailBuilder vietnameseId(String vietnameseId) { detail.vietnameseId = vietnameseId; return this; }
            public GraduationEligibilityDetailBuilder currentGpa(Double currentGpa) { detail.currentGpa = currentGpa; return this; }
            public GraduationEligibilityDetailBuilder completedCredits(Integer completedCredits) { detail.completedCredits = completedCredits; return this; }
            public GraduationEligibilityDetailBuilder failedCredits(Integer failedCredits) { detail.failedCredits = failedCredits; return this; }
            public GraduationEligibilityDetailBuilder eligible(Boolean eligible) { detail.eligible = eligible; return this; }
            public GraduationEligibilityDetailBuilder reason(String reason) { detail.reason = reason; return this; }
            public GraduationEligibilityDetail build() { return detail; }
        }
    }

    public static class AcademicPerformanceTrend {
        private Long facultyId;
        private Integer startYear;
        private Integer endYear;
        private Map<Integer, YearlyPerformance> yearlyData;
        private LocalDateTime generatedAt;

        public static AcademicPerformanceTrendBuilder builder() {
            return new AcademicPerformanceTrendBuilder();
        }

        // Getters and setters
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public Integer getStartYear() { return startYear; }
        public void setStartYear(Integer startYear) { this.startYear = startYear; }
        public Integer getEndYear() { return endYear; }
        public void setEndYear(Integer endYear) { this.endYear = endYear; }
        public Map<Integer, YearlyPerformance> getYearlyData() { return yearlyData; }
        public void setYearlyData(Map<Integer, YearlyPerformance> yearlyData) { this.yearlyData = yearlyData; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static class AcademicPerformanceTrendBuilder {
            private AcademicPerformanceTrend trend = new AcademicPerformanceTrend();
            public AcademicPerformanceTrendBuilder facultyId(Long facultyId) { trend.facultyId = facultyId; return this; }
            public AcademicPerformanceTrendBuilder startYear(Integer startYear) { trend.startYear = startYear; return this; }
            public AcademicPerformanceTrendBuilder endYear(Integer endYear) { trend.endYear = endYear; return this; }
            public AcademicPerformanceTrendBuilder yearlyData(Map<Integer, YearlyPerformance> yearlyData) { trend.yearlyData = yearlyData; return this; }
            public AcademicPerformanceTrendBuilder generatedAt(LocalDateTime generatedAt) { trend.generatedAt = generatedAt; return this; }
            public AcademicPerformanceTrend build() { return trend; }
        }
    }

    public static class YearlyPerformance {
        private Integer year;
        private Integer totalStudents;
        private Double averageGpa;
        private Integer goodStanding;
        private Integer probation;

        public static YearlyPerformanceBuilder builder() {
            return new YearlyPerformanceBuilder();
        }

        // Getters and setters
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Double getAverageGpa() { return averageGpa; }
        public void setAverageGpa(Double averageGpa) { this.averageGpa = averageGpa; }
        public Integer getGoodStanding() { return goodStanding; }
        public void setGoodStanding(Integer goodStanding) { this.goodStanding = goodStanding; }
        public Integer getProbation() { return probation; }
        public void setProbation(Integer probation) { this.probation = probation; }

        public static class YearlyPerformanceBuilder {
            private YearlyPerformance performance = new YearlyPerformance();
            public YearlyPerformanceBuilder year(Integer year) { performance.year = year; return this; }
            public YearlyPerformanceBuilder totalStudents(Integer totalStudents) { performance.totalStudents = totalStudents; return this; }
            public YearlyPerformanceBuilder averageGpa(Double averageGpa) { performance.averageGpa = averageGpa; return this; }
            public YearlyPerformanceBuilder goodStanding(Integer goodStanding) { performance.goodStanding = goodStanding; return this; }
            public YearlyPerformanceBuilder probation(Integer probation) { performance.probation = probation; return this; }
            public YearlyPerformance build() { return performance; }
        }
    }
}
