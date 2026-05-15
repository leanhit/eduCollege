package com.educollege.user.service;

import com.educollege.user.model.Student;
import com.educollege.user.repository.StudentRepository;
import com.educollege.academic.model.Faculty;
import com.educollege.academic.model.Department;
import com.educollege.academic.model.ClassGroup;
import com.educollege.academic.repository.FacultyRepository;
import com.educollege.academic.repository.DepartmentRepository;
import com.educollege.academic.repository.ClassGroupRepository;
import com.educollege.core.enums.StudentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.educollege.user.repository.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Student Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassGroupRepository classGroupRepository;
    
    public Student createStudent(Student student) {
        System.out.println("Creating student: " + student.getStudentNumber());
        
        Faculty faculty = facultyRepository.findById(student.getFaculty().getId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + student.getFaculty().getId()));
        
        student.setFaculty(faculty);
        
        if (student.getDepartment() != null) {
            Department department = departmentRepository.findById(student.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + student.getDepartment().getId()));
            student.setDepartment(department);
        }
        
        if (student.getClassGroup() != null) {
            ClassGroup classGroup = classGroupRepository.findById(student.getClassGroup().getId())
                .orElseThrow(() -> new RuntimeException("Class group not found with id: " + student.getClassGroup().getId()));
            student.setClassGroup(classGroup);
        }
        
        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new RuntimeException("Student with number " + student.getStudentNumber() + " already exists");
        }
        
        Student savedStudent = studentRepository.save(student);
        System.out.println("Student created successfully: " + savedStudent.getStudentNumber());
        return savedStudent;
    }
    
    public Student updateStudent(Long id, Student student) {
        System.out.println("Updating student with id: " + id);
        
        Student existingStudent = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        if (student.getFaculty() != null && 
            !existingStudent.getFaculty().getId().equals(student.getFaculty().getId())) {
            Faculty faculty = facultyRepository.findById(student.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + student.getFaculty().getId()));
            existingStudent.setFaculty(faculty);
        }
        
        if (student.getDepartment() != null && 
            (existingStudent.getDepartment() == null || 
             !existingStudent.getDepartment().getId().equals(student.getDepartment().getId()))) {
            Department department = departmentRepository.findById(student.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + student.getDepartment().getId()));
            existingStudent.setDepartment(department);
        }
        
        if (student.getClassGroup() != null && 
            (existingStudent.getClassGroup() == null || 
             !existingStudent.getClassGroup().getId().equals(student.getClassGroup().getId()))) {
            ClassGroup classGroup = classGroupRepository.findById(student.getClassGroup().getId())
                .orElseThrow(() -> new RuntimeException("Class group not found with id: " + student.getClassGroup().getId()));
            existingStudent.setClassGroup(classGroup);
        }
        
        if (!existingStudent.getStudentNumber().equals(student.getStudentNumber()) && 
            studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new RuntimeException("Student with number " + student.getStudentNumber() + " already exists");
        }
        
        existingStudent.setStudentNumber(student.getStudentNumber());
        existingStudent.setEnrollmentDate(student.getEnrollmentDate());
        existingStudent.setExpectedGraduationDate(student.getExpectedGraduationDate());
        existingStudent.setEnrollmentYear(student.getEnrollmentYear());
        existingStudent.setGraduationYear(student.getGraduationYear());
        existingStudent.setCurrentGpa(student.getCurrentGpa());
        existingStudent.setCumulativeGpa(student.getCumulativeGpa());
        existingStudent.setTotalCredits(student.getTotalCredits());
        existingStudent.setCompletedCredits(student.getCompletedCredits());
        existingStudent.setFailedCredits(student.getFailedCredits());
        existingStudent.setAcademicStanding(student.getAcademicStanding());
        existingStudent.setStudentStatus(student.getStudentStatus());
        existingStudent.setAdvisorId(student.getAdvisorId());
        existingStudent.setNotes(student.getNotes());
        existingStudent.setIsActive(student.getIsActive());
        
        Student updatedStudent = studentRepository.save(existingStudent);
        System.out.println("Student updated successfully: " + updatedStudent.getStudentNumber());
        return updatedStudent;
    }
    
    public void deleteStudent(Long id) {
        System.out.println("Deleting student with id: " + id);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        studentRepository.delete(student);
        System.out.println("Student deleted successfully: " + student.getStudentNumber());
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUser_Id(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByDepartmentId(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByClassGroupId(Long classGroupId) {
        return studentRepository.findByClassGroupId(classGroupId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByEnrollmentYear(Integer enrollmentYear) {
        return studentRepository.findByEnrollmentYear(enrollmentYear);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByGraduationYear(Integer graduationYear) {
        return studentRepository.findByGraduationYear(graduationYear);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByStudentStatus(StudentStatus studentStatus) {
        return studentRepository.findByStudentStatus(studentStatus);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByAcademicStanding(String academicStanding) {
        return studentRepository.findByAcademicStanding(academicStanding);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByAdvisorId(Long advisorId) {
        return studentRepository.findByAdvisorId(advisorId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudents() {
        return studentRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyIdAndIsActiveTrue(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByClassGroupId(Long classGroupId) {
        return studentRepository.findByClassGroupIdAndIsActiveTrue(classGroupId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByEnrollmentYear(Integer enrollmentYear) {
        return studentRepository.findByEnrollmentYearAndIsActiveTrue(enrollmentYear);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByStudentStatus(StudentStatus studentStatus) {
        return studentRepository.findByStudentStatusAndIsActiveTrue(studentStatus);
    }
    
    @Transactional(readOnly = true)
    public List<Student> searchStudentsByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumberContaining(studentNumber);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByFacultyAndYear(Long facultyId, Integer year) {
        return studentRepository.findActiveStudentsByFacultyAndYear(facultyId, year);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByClassGroup(Long classGroupId) {
        return studentRepository.findActiveStudentsByClassGroup(classGroupId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveAdvisees(Long advisorId) {
        return studentRepository.findActiveAdvisees(advisorId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getStudentsByGpaRange(Double minGpa, Double maxGpa) {
        return studentRepository.findByGpaRange(minGpa, maxGpa);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByAcademicStanding(String standing) {
        return studentRepository.findActiveStudentsByAcademicStanding(standing);
    }
    
    @Transactional(readOnly = true)
    public long countActiveStudentsByFaculty(Long facultyId) {
        return studentRepository.countActiveStudentsByFaculty(facultyId);
    }
    
    @Transactional(readOnly = true)
    public long countActiveStudentsByClassGroup(Long classGroupId) {
        return studentRepository.countActiveStudentsByClassGroup(classGroupId);
    }
    
    @Transactional(readOnly = true)
    public long countActiveAdvisees(Long advisorId) {
        return studentRepository.countActiveAdvisees(advisorId);
    }
    
    @Transactional(readOnly = true)
    public Double getAverageGpaByFaculty(Long facultyId) {
        return studentRepository.getAverageGpaByFaculty(facultyId);
    }
    
    @Transactional(readOnly = true)
    public List<Student> getActiveStudentsByStatus(StudentStatus status) {
        return studentRepository.findActiveStudentsByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public long countByFacultyId(Long facultyId) {
        return studentRepository.countByFacultyId(facultyId);
    }
    
    @Transactional(readOnly = true)
    public long countByClassGroupId(Long classGroupId) {
        return studentRepository.countByClassGroupId(classGroupId);
    }
    
    @Transactional(readOnly = true)
    public long countByEnrollmentYear(Integer enrollmentYear) {
        return studentRepository.countByEnrollmentYear(enrollmentYear);
    }
    
    @Transactional(readOnly = true)
    public long countByGraduationYear(Integer graduationYear) {
        return studentRepository.countByGraduationYear(graduationYear);
    }
    
    @Transactional(readOnly = true)
    public long countByStudentStatus(StudentStatus studentStatus) {
        return studentRepository.countByStudentStatus(studentStatus);
    }
    
    @Transactional(readOnly = true)
    public long countByAcademicStanding(String academicStanding) {
        return studentRepository.countByAcademicStanding(academicStanding);
    }
    
    @Transactional(readOnly = true)
    public long countByAdvisorId(Long advisorId) {
        return studentRepository.countByAdvisorId(advisorId);
    }
    
    @Transactional(readOnly = true)
    public long countByIsActiveTrue() {
        return studentRepository.countByIsActiveTrue();
    }
    
    public Student activateStudent(Long id) {
        System.out.println("Activating student with id: " + id);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        student.setIsActive(true);
        Student activatedStudent = studentRepository.save(student);
        System.out.println("Student activated successfully: " + activatedStudent.getStudentNumber());
        return activatedStudent;
    }
    
    public Student deactivateStudent(Long id) {
        System.out.println("Deactivating student with id: " + id);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        student.setIsActive(false);
        Student deactivatedStudent = studentRepository.save(student);
        System.out.println("Student deactivated successfully: " + deactivatedStudent.getStudentNumber());
        return deactivatedStudent;
    }
    
    public Student assignAdvisor(Long studentId, Long advisorId) {
        System.out.println("Assigning advisor " + advisorId + " to student " + studentId);
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        student.setAdvisorId(advisorId);
        
        Student updatedStudent = studentRepository.save(student);
        System.out.println("Advisor assigned successfully");
        return updatedStudent;
    }
    
    public Student updateGpa(Long id, Double currentGpa, Double cumulativeGpa) {
        System.out.println("Updating GPA for student with id: " + id);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        student.setCurrentGpa(currentGpa);
        student.setCumulativeGpa(cumulativeGpa);
        
        // Update academic standing based on GPA
        if (currentGpa >= 3.6) {
            student.setAcademicStanding("EXCELLENT");
        } else if (currentGpa >= 3.2) {
            student.setAcademicStanding("VERY_GOOD");
        } else if (currentGpa >= 2.5) {
            student.setAcademicStanding("GOOD");
        } else if (currentGpa >= 2.0) {
            student.setAcademicStanding("SATISFACTORY");
        } else {
            student.setAcademicStanding("POOR");
        }
        
        Student updatedStudent = studentRepository.save(student);
        System.out.println("GPA updated successfully");
        return updatedStudent;
    }
    
    public Student updateCredits(Long id, Integer totalCredits, Integer completedCredits, Integer failedCredits) {
        System.out.println("Updating credits for student with id: " + id);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        student.setTotalCredits(totalCredits);
        student.setCompletedCredits(completedCredits);
        student.setFailedCredits(failedCredits);
        
        Student updatedStudent = studentRepository.save(student);
        System.out.println("Credits updated successfully");
        return updatedStudent;
    }

    @Transactional(readOnly = true)
    public List<Student> searchStudents(Long facultyId, Long classGroupId, Integer year, String status, String keyword) {
        log.info("Searching students with facultyId={}, classGroupId={}, year={}, status={}, keyword={}", 
                 facultyId, classGroupId, year, status, keyword);

        Specification<Student> spec = Specification.where(StudentSpecification.hasFacultyId(facultyId))
                .and(StudentSpecification.hasClassGroupId(classGroupId))
                .and(StudentSpecification.hasEnrollmentYear(year))
                .and(StudentSpecification.hasStatus(status))
                .and(StudentSpecification.searchByNameOrCode(keyword));

        return studentRepository.findAll(spec);
    }
}
