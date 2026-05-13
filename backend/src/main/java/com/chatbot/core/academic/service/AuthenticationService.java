package com.chatbot.core.academic.service;

import com.chatbot.core.academic.dto.LoginRequest;
import com.chatbot.core.academic.dto.StudentRegistrationRequest;
import com.chatbot.core.academic.dto.TeacherRegistrationRequest;
import com.chatbot.core.academic.dto.UserResponse;
import com.chatbot.core.academic.dto.VietnameseIdLoginRequest;
import com.chatbot.core.academic.model.Faculty;
import com.chatbot.core.academic.model.Department;
import com.chatbot.core.academic.model.ClassGroup;
import com.chatbot.core.academic.model.Student;
import com.chatbot.core.academic.model.Teacher;
import com.chatbot.core.academic.repository.FacultyRepository;
import com.chatbot.core.academic.repository.DepartmentRepository;
import com.chatbot.core.academic.repository.ClassGroupRepository;
import com.chatbot.core.academic.repository.StudentRepository;
import com.chatbot.core.academic.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Authentication Service for Vietnamese Academic System
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {
    
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VietnameseIdService vietnameseIdService;
    private final VietnameseAcademicValidationService validationService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassGroupRepository classGroupRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    /**
     * Register new student with Vietnamese ID generation
     */
    public UserResponse registerStudent(StudentRegistrationRequest request) {
        System.out.println("Registering new student: " + request.getEmail());
        
        // Validate request
        if (!request.isValid()) {
            throw new RuntimeException("Invalid registration request");
        }
        
        // Validate Vietnamese data
        validationService.validateVietnameseName(request.getVietnameseName());
        validationService.validateVietnamesePhone(request.getPhone());
        validationService.validateVietnameseEmail(request.getEmail());
        validationService.validateVietnameseIdNumber(request.getIdNumber());
        validationService.validateVietnameseAddress(request.getAddress());
        validationService.validateAcademicLevel(request.getAcademicLevel());
        
        // Check if email already exists
        if (emailExists(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Validate faculty and class
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + request.getFacultyId()));
        
        ClassGroup classGroup = classGroupRepository.findById(request.getClassId())
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + request.getClassId()));
        
        // Generate Vietnamese student ID
        String vietnameseId = vietnameseIdService.generateStudentId(faculty, classGroup, request.getEnrollmentYear());
        
        // Validate Vietnamese ID uniqueness
        if (vietnameseIdExists(vietnameseId)) {
            throw new RuntimeException("Vietnamese ID already exists: " + vietnameseId);
        }
        
        // Create student entity
        Student student = Student.builder()
            .userId(null) // Will be set when User model is available
            .studentNumber(vietnameseId)
            .faculty(faculty)
            .classGroup(classGroup)
            .enrollmentDate(java.time.LocalDate.now())
            .expectedGraduationDate(java.time.LocalDate.of(request.getEnrollmentYear() + 4, 9, 1))
            .enrollmentYear(request.getEnrollmentYear())
            .graduationYear(request.getEnrollmentYear() + 4)
            .currentGpa(0.0)
            .cumulativeGpa(0.0)
            .totalCredits(0)
            .completedCredits(0)
            .failedCredits(0)
            .academicStanding("GOOD")
            .studentStatus(com.chatbot.core.academic.enums.StudentStatus.ENROLLED)
            .notes(request.getNotes())
            .isActive(true)
            .build();
        
        // Save student
        student = studentRepository.save(student);
        
        // Generate JWT tokens
        String token = jwtService.generateToken(request.getUsername(), vietnameseId, "STUDENT", "SINHVIEN");
        String refreshToken = jwtService.generateRefreshToken(request.getUsername(), vietnameseId);
        
        // Create response
        UserResponse response = UserResponse.builder()
            .id(student.getId())
            .email(request.getEmail())
            .username(request.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("SINHVIEN")
            .academicLevel(request.getAcademicLevel())
            .fullName(request.getFullName())
            .vietnameseName(request.getVietnameseName())
            .phone(request.getPhone())
            .address(request.getAddress())
            .dateOfBirth(request.getDateOfBirth().toString())
            .placeOfBirth(request.getPlaceOfBirth())
            .nationality(request.getNationality())
            .systemRole("USER")
            .status("ACTIVE")
            .facultyId(faculty.getId())
            .facultyCode(faculty.getCode())
            .facultyName(faculty.getName())
            .classId(classGroup.getId())
            .classCode(classGroup.getCode())
            .className(classGroup.getName())
            .enrollmentYear(request.getEnrollmentYear())
            .graduationYear(request.getEnrollmentYear() + 4)
            .academicStanding("GOOD")
            .currentGpa(0.0)
            .cumulativeGpa(0.0)
            .totalCredits(0)
            .completedCredits(0)
            .failedCredits(0)
            .studentStatus("ENROLLED")
            .studentNumber(vietnameseId)
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getExpirationTime() / 1000))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationTime() / 1000))
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
        
        System.out.println("Student registered successfully: " + vietnameseId);
        return response;
    }
    
    /**
     * Register new teacher with Vietnamese ID generation
     */
    public UserResponse registerTeacher(TeacherRegistrationRequest request) {
        System.out.println("Registering new teacher: " + request.getEmail());
        
        // Validate request
        if (!request.isValid()) {
            throw new RuntimeException("Invalid registration request");
        }
        
        // Validate Vietnamese data
        validationService.validateVietnameseName(request.getVietnameseName());
        validationService.validateVietnamesePhone(request.getPhone());
        validationService.validateVietnameseEmail(request.getEmail());
        validationService.validateVietnameseIdNumber(request.getIdNumber());
        validationService.validateVietnameseAddress(request.getAddress());
        validationService.validateAcademicLevel(request.getAcademicLevel());
        
        // Check if email already exists
        if (emailExists(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Validate faculty and department
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + request.getFacultyId()));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
        
        // Generate Vietnamese teacher ID
        String vietnameseId = vietnameseIdService.generateTeacherId(department);
        
        // Validate Vietnamese ID uniqueness
        if (vietnameseIdExists(vietnameseId)) {
            throw new RuntimeException("Vietnamese ID already exists: " + vietnameseId);
        }
        
        // Create teacher entity
        Teacher teacher = Teacher.builder()
            .userId(null) // Will be set when User model is available
            .teacherNumber(vietnameseId)
            .department(department)
            .academicTitle(request.getAcademicTitle())
            .hireDate(request.getHireDate())
            .specialization(request.getSpecialization())
            .researchInterests(request.getResearchInterests())
            .officeLocation(request.getOfficeLocation())
            .officePhone(request.getOfficePhone())
            .mobilePhone(request.getMobilePhone())
            .email(request.getEmail())
            .maxCoursesPerSemester(6)
            .currentCoursesPerSemester(0)
            .isAdvisor(true)
            .maxAdvisees(30)
            .currentAdvisees(0)
            .isActive(true)
            .build();
        
        // Save teacher
        teacher = teacherRepository.save(teacher);
        
        // Generate JWT tokens
        String token = jwtService.generateToken(request.getUsername(), vietnameseId, "TEACHER", "GIAOVIEN");
        String refreshToken = jwtService.generateRefreshToken(request.getUsername(), vietnameseId);
        
        // Create response
        UserResponse response = UserResponse.builder()
            .id(teacher.getId())
            .email(request.getEmail())
            .username(request.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("GIAOVIEN")
            .academicLevel(request.getAcademicLevel())
            .fullName(request.getFullName())
            .vietnameseName(request.getVietnameseName())
            .phone(request.getPhone())
            .address(request.getAddress())
            .dateOfBirth(request.getDateOfBirth().toString())
            .placeOfBirth(request.getPlaceOfBirth())
            .nationality(request.getNationality())
            .systemRole("TEACHER")
            .status("ACTIVE")
            .facultyId(faculty.getId())
            .facultyCode(faculty.getCode())
            .facultyName(faculty.getName())
            .departmentId(department.getId())
            .departmentCode(department.getCode())
            .departmentName(department.getName())
            .academicTitle(request.getAcademicTitle())
            .specialization(request.getSpecialization())
            .officeLocation(request.getOfficeLocation())
            .officePhone(request.getOfficePhone())
            .researchInterests(request.getResearchInterests())
            .hireDate(request.getHireDate())
            .teacherNumber(vietnameseId)
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getExpirationTime() / 1000))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationTime() / 1000))
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
        
        System.out.println("Teacher registered successfully: " + vietnameseId);
        return response;
    }
    
    /**
     * Login with username or Vietnamese ID
     */
    public UserResponse login(LoginRequest request) {
        System.out.println("Login attempt: " + request.getUsernameOrVietnameseId());
        
        // Validate request
        if (!request.isValid()) {
            throw new RuntimeException("Invalid login request");
        }
        
        // Determine login type and authenticate
        UserResponse userResponse;
        if (request.isVietnameseIdLogin()) {
            userResponse = loginByVietnameseId(request.getUsernameOrVietnameseId(), request.getPassword());
        } else {
            userResponse = loginByUsername(request.getUsernameOrVietnameseId(), request.getPassword());
        }
        
        // Update login tracking
        updateLoginTracking(userResponse, request);
        
        System.out.println("Login successful: " + userResponse.getVietnameseId());
        return userResponse;
    }
    
    /**
     * Login with Vietnamese ID
     */
    public UserResponse loginByVietnameseId(VietnameseIdLoginRequest request) {
        System.out.println("Vietnamese ID login attempt: " + request.getVietnameseId());
        
        // Validate Vietnamese ID format
        vietnameseIdService.validateStudentId(request.getVietnameseId());
        
        // Authenticate and get user
        UserResponse userResponse = loginByVietnameseId(request.getVietnameseId(), request.getPassword());
        
        // Update login tracking
        updateLoginTracking(userResponse, request);
        
        System.out.println("Vietnamese ID login successful: " + userResponse.getVietnameseId());
        return userResponse;
    }
    
    /**
     * Refresh token
     */
    public UserResponse refreshToken(String refreshToken) {
        System.out.println("Refreshing token");
        
        // Validate refresh token
        if (!jwtService.isTokenValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        // Extract user info from token
        String username = jwtService.extractUsername(refreshToken);
        String vietnameseId = jwtService.extractVietnameseId(refreshToken);
        String role = jwtService.extractRole(refreshToken);
        String idCategory = jwtService.extractIdCategory(refreshToken);
        
        // Generate new tokens
        String newToken = jwtService.generateToken(username, vietnameseId, role, idCategory);
        String newRefreshToken = jwtService.generateRefreshToken(username, vietnameseId);
        
        // Create response
        UserResponse response = UserResponse.builder()
            .username(username)
            .vietnameseId(vietnameseId)
            .idCategory(idCategory)
            .systemRole(role)
            .token(newToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getExpirationTime() / 1000))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationTime() / 1000))
            .updatedAt(java.time.LocalDateTime.now())
            .build();
        
        System.out.println("Token refreshed successfully");
        return response;
    }
    
    /**
     * Validate token
     */
    public Boolean validateToken(String token) {
        return jwtService.isTokenValid(token);
    }
    
    /**
     * Get user info from token
     */
    public UserResponse getUserFromToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid token");
        }
        
        String username = jwtService.extractUsername(token);
        String vietnameseId = jwtService.extractVietnameseId(token);
        String role = jwtService.extractRole(token);
        String idCategory = jwtService.extractIdCategory(token);
        
        return UserResponse.builder()
            .username(username)
            .vietnameseId(vietnameseId)
            .idCategory(idCategory)
            .systemRole(role)
            .build();
    }
    
    // Private helper methods
    
    private UserResponse loginByVietnameseId(String vietnameseId, String password) {
        // Check if it's student or teacher ID
        if (vietnameseId.startsWith("SV")) {
            return loginStudentByVietnameseId(vietnameseId, password);
        } else if (vietnameseId.startsWith("GV")) {
            return loginTeacherByVietnameseId(vietnameseId, password);
        } else {
            throw new RuntimeException("Invalid Vietnamese ID format");
        }
    }
    
    private UserResponse loginStudentByVietnameseId(String vietnameseId, String password) {
        Student student = studentRepository.findByStudentNumber(vietnameseId)
            .orElseThrow(() -> new RuntimeException("Student not found with Vietnamese ID: " + vietnameseId));
        
        // In production, verify password against User model
        // For now, simulate password verification
        if (!verifyPassword(password, "demo-password")) {
            throw new RuntimeException("Invalid password");
        }
        
        // Generate tokens
        String token = jwtService.generateToken("student" + student.getId(), vietnameseId, "STUDENT", "SINHVIEN");
        String refreshToken = jwtService.generateRefreshToken("student" + student.getId(), vietnameseId);
        
        return UserResponse.fromDemo(student)
            .toBuilder()
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getExpirationTime() / 1000))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationTime() / 1000))
            .build();
    }
    
    private UserResponse loginTeacherByVietnameseId(String vietnameseId, String password) {
        Teacher teacher = teacherRepository.findByTeacherNumber(vietnameseId)
            .orElseThrow(() -> new RuntimeException("Teacher not found with Vietnamese ID: " + vietnameseId));
        
        // In production, verify password against User model
        // For now, simulate password verification
        if (!verifyPassword(password, "demo-password")) {
            throw new RuntimeException("Invalid password");
        }
        
        // Generate tokens
        String token = jwtService.generateToken("teacher" + teacher.getId(), vietnameseId, "TEACHER", "GIAOVIEN");
        String refreshToken = jwtService.generateRefreshToken("teacher" + teacher.getId(), vietnameseId);
        
        return UserResponse.fromDemo(teacher)
            .toBuilder()
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .tokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getExpirationTime() / 1000))
            .refreshTokenExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationTime() / 1000))
            .build();
    }
    
    private UserResponse loginByUsername(String username, String password) {
        // In production, find user by username from User model
        // For now, simulate lookup and return demo response
        if (!verifyPassword(password, "demo-password")) {
            throw new RuntimeException("Invalid password");
        }
        
        return UserResponse.createDemoUser(username + "@demo.com", "DEMO" + UUID.randomUUID().toString().substring(0, 8), "USER");
    }
    
    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        // In production, use passwordEncoder.matches(rawPassword, encodedPassword)
        // For now, simulate verification
        return "demo-password".equals(rawPassword);
    }
    
    private boolean emailExists(String email) {
        // In production, check User model
        // For now, return false to allow demo registration
        return false;
    }
    
    private boolean vietnameseIdExists(String vietnameseId) {
        // Check both student and teacher repositories
        return studentRepository.findByStudentNumber(vietnameseId).isPresent() ||
               teacherRepository.findByTeacherNumber(vietnameseId).isPresent();
    }
    
    private void updateLoginTracking(UserResponse userResponse, Object request) {
        // Update login tracking information
        userResponse.setLastLoginAt(java.time.LocalDateTime.now());
        
        // Extract device info if available
        if (request instanceof LoginRequest) {
            LoginRequest loginRequest = (LoginRequest) request;
            userResponse.setLastLoginIp(loginRequest.getIpAddress());
            userResponse.setLastLoginDevice(loginRequest.getDeviceType());
            userResponse.setLastLoginLocation(loginRequest.getLocation());
        } else if (request instanceof VietnameseIdLoginRequest) {
            VietnameseIdLoginRequest vietnameseLoginRequest = (VietnameseIdLoginRequest) request;
            userResponse.setLastLoginIp(vietnameseLoginRequest.getIpAddress());
            userResponse.setLastLoginDevice(vietnameseLoginRequest.getDeviceType());
            userResponse.setLastLoginLocation(vietnameseLoginRequest.getLocation());
        }
    }
}
