package com.educollege.auth.service;

import com.educollege.academic.service.VietnameseIdService;
import com.educollege.academic.service.VietnameseAcademicValidationService;
import com.educollege.auth.dto.LoginRequest;
import com.educollege.user.dto.StudentRegistrationRequest;
import com.educollege.user.dto.TeacherRegistrationRequest;
import com.educollege.user.dto.UserResponse;
import com.educollege.auth.dto.VietnameseIdLoginRequest;
import com.educollege.academic.model.Faculty;
import com.educollege.academic.model.Department;
import com.educollege.academic.model.ClassGroup;
import com.educollege.user.model.Student;
import com.educollege.user.model.Teacher;
import com.educollege.user.model.User;
import com.educollege.core.enums.Role;
import com.educollege.academic.repository.FacultyRepository;
import com.educollege.academic.repository.DepartmentRepository;
import com.educollege.academic.repository.ClassGroupRepository;
import com.educollege.user.repository.StudentRepository;
import com.educollege.user.repository.TeacherRepository;
import com.educollege.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private final UserRepository userRepository;
    
    public UserResponse registerStudent(StudentRegistrationRequest request) {
        log.info("Registering new student: {}", request.getEmail());
        
        if (!request.isValid()) {
            throw new RuntimeException("Invalid registration request");
        }
        
        validationService.validateVietnameseName(request.getVietnameseName());
        validationService.validateVietnamesePhone(request.getPhone());
        validationService.validateVietnameseEmail(request.getEmail());
        validationService.validateVietnameseIdNumber(request.getIdNumber());
        validationService.validateVietnameseAddress(request.getAddress());
        validationService.validateAcademicLevel(request.getAcademicLevel());
        
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with email or username already exists");
        }
        
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + request.getFacultyId()));
        
        ClassGroup classGroup = classGroupRepository.findById(request.getClassId())
            .orElseThrow(() -> new RuntimeException("Class group not found with id: " + request.getClassId()));
        
        String vietnameseId = vietnameseIdService.generateStudentId(faculty, classGroup, request.getEnrollmentYear());
        
        if (studentRepository.findByStudentNumber(vietnameseId).isPresent()) {
            throw new RuntimeException("Vietnamese ID already exists: " + vietnameseId);
        }
        
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .vietnameseId(vietnameseId)
            .idCategory("SINHVIEN")
            .academicLevel(com.educollege.core.enums.AcademicLevel.valueOf(request.getAcademicLevel()))
            .faculty(faculty)
            .classGroup(classGroup)
            .enrollmentYear(request.getEnrollmentYear())
            .graduationYear(request.getEnrollmentYear() + 4)
            .studentStatus(com.educollege.core.enums.StudentStatus.ENROLLED)
            .role(Role.STUDENT)
            .build();
        user = userRepository.save(user);
        
        Student student = Student.builder()
            .user(user)
            .studentNumber(vietnameseId)
            .faculty(faculty)
            .classGroup(classGroup)
            .enrollmentDate(LocalDate.now())
            .expectedGraduationDate(LocalDate.of(request.getEnrollmentYear() + 4, 9, 1))
            .enrollmentYear(request.getEnrollmentYear())
            .graduationYear(request.getEnrollmentYear() + 4)
            .currentGpa(0.0)
            .cumulativeGpa(0.0)
            .totalCredits(0)
            .completedCredits(0)
            .failedCredits(0)
            .academicStanding("GOOD")
            .studentStatus(com.educollege.core.enums.StudentStatus.ENROLLED)
            .notes(request.getNotes())
            .isActive(true)
            .build();
        
        student = studentRepository.save(student);
        
        String token = jwtService.generateToken(user.getUsername(), vietnameseId, "STUDENT", "SINHVIEN");
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), vietnameseId);
        
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("SINHVIEN")
            .academicLevel(request.getAcademicLevel())
            .fullName(request.getFullName())
            .systemRole("USER")
            .status("ACTIVE")
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
    }
    
    public UserResponse registerTeacher(TeacherRegistrationRequest request) {
        log.info("Registering new teacher: {}", request.getEmail());
        
        if (!request.isValid()) {
            throw new RuntimeException("Invalid registration request");
        }
        
        validationService.validateVietnameseName(request.getVietnameseName());
        
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with email or username already exists");
        }
        
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + request.getFacultyId()));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
        
        String vietnameseId = vietnameseIdService.generateTeacherId(department);
        
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .vietnameseId(vietnameseId)
            .idCategory("GIAOVIEN")
            .faculty(faculty)
            .department(department)
            .role(Role.TEACHER)
            .build();
        user = userRepository.save(user);
        
        Teacher teacher = Teacher.builder()
            .user(user)
            .teacherNumber(vietnameseId)
            .department(department)
            .academicTitle(request.getAcademicTitle())
            .hireDate(request.getHireDate())
            .specialization(request.getSpecialization())
            .email(request.getEmail())
            .isActive(true)
            .isAdvisor(true)
            .build();
            
        teacher = teacherRepository.save(teacher);
        
        String token = jwtService.generateToken(user.getUsername(), vietnameseId, "TEACHER", "GIAOVIEN");
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), vietnameseId);
        
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("GIAOVIEN")
            .systemRole("TEACHER")
            .status("ACTIVE")
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
    }
    
    public UserResponse login(LoginRequest request) {
        log.info("Login attempt: {}", request.getUsernameOrVietnameseId());
        
        if (!request.isValid()) {
            throw new RuntimeException("Invalid login request");
        }
        
        User user;
        String vietnameseId = null;
        String idCategory = "SINHVIEN";
        String roleStr = "USER";
        
        if (request.isVietnameseIdLogin()) {
            String vid = request.getUsernameOrVietnameseId();
            validationService.validateStudentId(vid);
            vietnameseId = vid;
            if (vid.startsWith("SV")) {
                Student student = studentRepository.findByStudentNumber(vid)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
                user = student.getUser();
                idCategory = "SINHVIEN";
                roleStr = "STUDENT";
            } else if (vid.startsWith("GV")) {
                Teacher teacher = teacherRepository.findByTeacherNumber(vid)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
                user = teacher.getUser();
                idCategory = "GIAOVIEN";
                roleStr = "TEACHER";
            } else {
                throw new RuntimeException("Invalid ID prefix");
            }
        } else {
            user = userRepository.findByUsername(request.getUsernameOrVietnameseId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            roleStr = user.getRole().name();
            idCategory = user.getRole() == Role.TEACHER ? "GIAOVIEN" : (user.getRole() == Role.ADMIN ? "NHANVIEN" : "SINHVIEN");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtService.generateToken(user.getUsername(), vietnameseId != null ? vietnameseId : "", roleStr, idCategory);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), vietnameseId != null ? vietnameseId : "");
        
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .vietnameseId(vietnameseId)
            .systemRole(roleStr)
            .idCategory(idCategory)
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
    }
    
    public UserResponse loginByVietnameseId(VietnameseIdLoginRequest request) {
        LoginRequest lr = new LoginRequest();
        lr.setUsernameOrVietnameseId(request.getVietnameseId());
        lr.setPassword(request.getPassword());
        return login(lr);
    }
    
    public UserResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String username = jwtService.extractUsername(refreshToken);
        String vietnameseId = jwtService.extractVietnameseId(refreshToken);
        String role = jwtService.extractRole(refreshToken);
        String idCategory = jwtService.extractIdCategory(refreshToken);
        
        String newToken = jwtService.generateToken(username, vietnameseId, role, idCategory);
        String newRefreshToken = jwtService.generateRefreshToken(username, vietnameseId);
        
        return UserResponse.builder()
            .username(username)
            .vietnameseId(vietnameseId)
            .idCategory(idCategory)
            .systemRole(role)
            .token(newToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .build();
    }
    
    public Boolean validateToken(String token) {
        return jwtService.isTokenValid(token);
    }
    
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
}
