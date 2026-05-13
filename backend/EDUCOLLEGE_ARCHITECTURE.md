# EduCollege Architecture Design

## Tổng quan

Thiết kế hệ thống quản lý đại học eduCollege dựa trên best practices từ các trường đại học Việt Nam (ĐH Bách Khoa, ĐH KHTN, ĐH Ngoại thương, ĐH FPT) kết hợp với architecture hiện tại.

## 1. Overall Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Layer                        │
│  - Student Portal                                        │
│  - Teacher Portal                                        │
│  - Admin Dashboard                                       │
└─────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────┐
│                    API Gateway                           │
│  - Authentication & Authorization                      │
│  - Rate Limiting                                        │
│  - Request Routing                                      │
└─────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────┐
│                  Microservices Layer                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │
│  │   Auth      │ │   User      │ │   Academic          │ │
│  │   Service   │ │   Service   │ │   Service           │ │
│  └─────────────┘ └─────────────┘ └─────────────────────┘ │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │
│  │   Tenant    │ │   Course    │ │   Enrollment        │ │
│  │   Service   │ │   Service   │ │   Service           │ │
│  └─────────────┘ └─────────────┘ └─────────────────────┘ │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │
│  │   Schedule  │ │   Grade     │ │   Report            │ │
│  │   Service   │ │   Service   │ │   Service           │ │
│  └─────────────┘ └─────────────┘ └─────────────────────┘ │
└─────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────┐
│                  Data Layer                               │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │
│  │   User DB   │ │ Academic DB │ │   Tenant DB         │ │
│  │ (PostgreSQL)│ │ (PostgreSQL)│ │ (PostgreSQL)        │ │
│  └─────────────┘ └─────────────┘ └─────────────────────┘ │
└─────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────┐
│                Infrastructure Layer                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │
│  │   Redis     │ │   MinIO     │ │   RabbitMQ          │ │
│  │   Cache     │ │   Storage   │ │   Messaging         │ │
│  └─────────────┘ └─────────────┘ └─────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## 2. Domain Model Design

### 2.1 Core Entities

#### User Management
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    // Vietnamese ID System
    @Column(name = "vietnamese_id", nullable = false)
    private String vietnameseId; // "SV24CNTT00101"
    
    @Column(name = "id_category", nullable = false)
    private String idCategory; // "SINHVIEN", "GIAOVIEN", "NHANVIEN"
    
    @Column(name = "academic_level")
    private String academicLevel; // "DAIHOC", "THACSI", "TIENSI"
    
    // Tenant Relationships
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;
    
    // Academic Info
    @Column(name = "enrollment_year")
    private Integer enrollmentYear;
    
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus; // ENROLLED, GRADUATED, DROPPED
    
    // Authentication
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "system_role", nullable = false)
    private SystemRole systemRole;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

#### Academic Structure
```java
@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code; // "CNTT", "TOAN", "LY"
    
    @Column(nullable = false)
    private String name; // "Khoa Công nghệ Thông tin"
    
    @Column(name = "vietnamese_name", nullable = false)
    private String vietnameseName; // "Khoa Công nghệ Thông tin"
    
    @Column(name = "english_name")
    private String englishName;
    
    @OneToMany(mappedBy = "faculty")
    private List<Department> departments;
    
    @OneToMany(mappedBy = "faculty")
    private List<ClassGroup> classes;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code; // "CNPM", "MMTT", "HTTT"
    
    @Column(nullable = false)
    private String name; // "Bộ môn Công nghệ Phần mềm"
    
    @Column(name = "vietnamese_name", nullable = false)
    private String vietnameseName;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @OneToMany(mappedBy = "department")
    private List<Teacher> teachers;
    
    @OneToMany(mappedBy = "department")
    private List<Course> courses;
}

@Entity
@Table(name = "class_groups")
public class ClassGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code; // "DH21CNTT01", "CLC2101"
    
    @Column(nullable = false)
    private String name; // "Lớp DH21CNTT01"
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;
    
    @Column(name = "enrollment_year", nullable = false)
    private Integer enrollmentYear;
    
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    @Column(name = "max_students")
    private Integer maxStudents = 50;
    
    @Column(name = "current_students")
    private Integer currentStudents = 0;
    
    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Teacher advisor;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code; // "TIN101", "TIN102"
    
    @Column(nullable = false)
    private String name; // "Lập trình C cơ bản"
    
    @Column(name = "vietnamese_name", nullable = false)
    private String vietnameseName; // "Lập trình C cơ bản"
    
    @Column(name = "english_name")
    private String englishName;
    
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @Column(nullable = false)
    private Integer credits;
    
    @Column(name = "theory_hours")
    private Integer theoryHours;
    
    @Column(name = "practice_hours")
    private Integer practiceHours;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "course_type", nullable = false)
    private CourseType courseType; // REQUIRED, ELECTIVE, OPTIONAL
    
    @Column(name = "description")
    private String description;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

@Entity
@Table(name = "course_offerings")
public class CourseOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code; // "TIN101-NH01-CL01"
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;
    
    @Column(name = "current_students")
    private Integer currentStudents = 0;
    
    @Column(name = "schedule", columnDefinition = "JSONB")
    private String schedule; // JSON schedule information
    
    @Column(name = "classroom")
    private String classroom;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseOfferingStatus status; // SCHEDULED, OPEN, CLOSED, CANCELLED
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_offering_id", nullable = false)
    private CourseOffering courseOffering;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status; // ENROLLED, COMPLETED, FAILED, DROPPED
    
    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Column(name = "grade")
    private Double grade;
    
    @Column(name = "letter_grade")
    private String letterGrade;
    
    @Column(name = "gpa_points")
    private Double gpaPoints;
    
    @Column(name = "attendance_rate")
    private Double attendanceRate;
    
    @Column(name = "notes")
    private String notes;
}
```

### 2.2 Vietnamese ID System

#### ID Format Patterns
```
Student ID: SV{year}{faculty}{sequence:5d}
- SV24CNTT00101 (Sinh viên 2024, CNTT, sequence 00101)
- SV24TOAN00050 (Sinh viên 2024, Toán, sequence 00050)

Teacher ID: GV{department}{sequence:4d}
- GVCNPM0001 (Giảng viên CNPM, sequence 0001)
- GVMMTT0005 (Giảng viên MMTT, sequence 0005)

Staff ID: NV{faculty}{dept-type}{sequence:4d}
- NVCNTTHC0001 (Nhân viên CNTT Hành chính, sequence 0001)
- NVTOANVT0002 (Nhân viên Toán Vật tư, sequence 0002)
```

#### ID Generation Service
```java
@Service
public class VietnameseIdService {
    
    public String generateStudentId(Faculty faculty, ClassGroup classGroup, Integer enrollmentYear) {
        String facultyCode = faculty.getCode(); // "CNTT"
        String year = String.valueOf(enrollmentYear).substring(2); // "24"
        String sequence = getNextSequence(faculty, classGroup); // "00101"
        
        return String.format("SV%s%s%05d", year, facultyCode, sequence);
    }
    
    public String generateTeacherId(Department department) {
        String deptCode = department.getCode(); // "CNPM"
        String sequence = getNextTeacherSequence(department); // "0001"
        
        return String.format("GV%s%04d", deptCode, sequence);
    }
    
    public String generateStaffId(Faculty faculty, String departmentType) {
        String facultyCode = faculty.getCode(); // "CNTT"
        String deptType = departmentType.substring(0, 2); // "HC" for Hành chính
        String sequence = getNextStaffSequence(faculty, departmentType); // "0001"
        
        return String.format("NV%s%s%04d", facultyCode, deptType, sequence);
    }
    
    private String getNextSequence(Faculty faculty, ClassGroup classGroup) {
        // Logic to get next sequence for student in faculty/class
        return String.format("%05d", sequenceRepository.getNextStudentSequence(faculty.getId(), classGroup.getId()));
    }
    
    private String getNextTeacherSequence(Department department) {
        // Logic to get next sequence for teacher in department
        return String.format("%04d", sequenceRepository.getNextTeacherSequence(department.getId()));
    }
    
    private String getNextStaffSequence(Faculty faculty, String departmentType) {
        // Logic to get next sequence for staff in faculty/department type
        return String.format("%04d", sequenceRepository.getNextStaffSequence(faculty.getId(), departmentType));
    }
}
```

## 3. Service Layer Architecture

### 3.1 Core Services

#### Authentication Service
```java
@Service
@Transactional
public class TenantAwareAuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VietnameseIdService vietnameseIdService;
    private final FacultyRepository facultyRepository;
    private final ClassGroupRepository classGroupRepository;
    
    public UserResponse registerStudent(StudentRegistrationRequest request) {
        // Validate tenant existence
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new EntityNotFoundException("Faculty not found"));
            
        ClassGroup classGroup = classGroupRepository.findById(request.getClassId())
            .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        
        // Generate Vietnamese ID
        String vietnameseId = vietnameseIdService.generateStudentId(
            faculty, classGroup, request.getEnrollmentYear()
        );
        
        // Validate ID uniqueness
        if (userRepository.existsByVietnameseId(vietnameseId)) {
            throw new ValidationException("Vietnamese ID already exists");
        }
        
        // Create user
        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("SINHVIEN")
            .academicLevel("DAIHOC")
            .faculty(faculty)
            .classGroup(classGroup)
            .enrollmentYear(request.getEnrollmentYear())
            .studentStatus(StudentStatus.ENROLLED)
            .password(passwordEncoder.encode(request.getPassword()))
            .systemRole(SystemRole.USER)
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        user = userRepository.save(user);
        
        // Create student profile
        Student student = Student.builder()
            .user(user)
            .studentNumber(vietnameseId)
            .classGroup(classGroup)
            .enrollmentDate(LocalDate.now())
            .build();
        
        studentRepository.save(student);
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        return UserResponse.from(user, token, refreshToken.getToken());
    }
    
    public UserResponse registerTeacher(TeacherRegistrationRequest request) {
        // Similar logic for teacher registration
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
            .orElseThrow(() -> new EntityNotFoundException("Faculty not found"));
            
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        String vietnameseId = vietnameseIdService.generateTeacherId(department);
        
        // Create teacher user
        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .vietnameseId(vietnameseId)
            .idCategory("GIAOVIEN")
            .academicLevel(request.getAcademicLevel())
            .faculty(faculty)
            .department(department)
            .password(passwordEncoder.encode(request.getPassword()))
            .systemRole(SystemRole.TEACHER)
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        user = userRepository.save(user);
        
        // Create teacher profile
        Teacher teacher = Teacher.builder()
            .user(user)
            .teacherNumber(vietnameseId)
            .department(department)
            .hireDate(LocalDate.now())
            .build();
        
        teacherRepository.save(teacher);
        
        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        return UserResponse.from(user, token, refreshToken.getToken());
    }
}
```

#### Academic Service
```java
@Service
@Transactional
public class AcademicService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final StudentRepository studentRepository;
    private final VietnameseValidationService validationService;
    
    public EnrollmentResponse enrollStudent(Long studentId, Long courseOfferingId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            
        CourseOffering offering = courseOfferingRepository.findById(courseOfferingId)
            .orElseThrow(() -> new EntityNotFoundException("Course offering not found"));
        
        // Validate enrollment rules
        validateEnrollmentRules(student, offering);
        
        // Check for existing enrollment
        if (enrollmentRepository.existsByStudentAndCourseOffering(student, offering)) {
            throw new ValidationException("Student already enrolled in this course");
        }
        
        // Create enrollment
        Enrollment enrollment = Enrollment.builder()
            .student(student)
            .courseOffering(offering)
            .status(EnrollmentStatus.ENROLLED)
            .enrollmentDate(LocalDateTime.now())
            .build();
        
        enrollment = enrollmentRepository.save(enrollment);
        
        // Update course offering count
        offering.setCurrentStudents(offering.getCurrentStudents() + 1);
        courseOfferingRepository.save(offering);
        
        return EnrollmentResponse.from(enrollment);
    }
    
    private void validateEnrollmentRules(Student student, CourseOffering offering) {
        // Check prerequisites
        if (!hasPrerequisites(student, offering.getCourse())) {
            throw new ValidationException("Student has not completed prerequisites");
        }
        
        // Check capacity
        if (offering.getCurrentStudents() >= offering.getMaxStudents()) {
            throw new ValidationException("Course is full");
        }
        
        // Check schedule conflicts
        if (hasScheduleConflict(student, offering)) {
            throw new ValidationException("Schedule conflict detected");
        }
        
        // Check credit limit
        int currentCredits = getCurrentCredits(student, offering.getSemester());
        int maxCredits = getMaxCreditsForStudent(student);
        if (currentCredits + offering.getCourse().getCredits() > maxCredits) {
            throw new ValidationException("Credit limit exceeded");
        }
        
        // Check academic standing
        if (!hasGoodAcademicStanding(student)) {
            throw new ValidationException("Student does not meet academic requirements");
        }
    }
    
    public List<EnrollmentResponse> getStudentEnrollments(Long studentId, Long semesterId) {
        List<Enrollment> enrollments = enrollmentRepository
            .findByStudentIdAndSemesterId(studentId, semesterId);
        return enrollments.stream()
            .map(EnrollmentResponse::from)
            .collect(Collectors.toList());
    }
    
    public List<EnrollmentResponse> getCourseEnrollments(Long courseOfferingId) {
        List<Enrollment> enrollments = enrollmentRepository
            .findByCourseOfferingId(courseOfferingId);
        return enrollments.stream()
            .map(EnrollmentResponse::from)
            .collect(Collectors.toList());
    }
}
```

### 3.2 Vietnamese Academic Validation Service
```java
@Service
public class VietnameseAcademicValidationService {
    
    public void validateStudentId(String studentId) {
        // Vietnamese student ID format: SVYYFACULTYSEQUENCE
        if (!studentId.matches("^SV[0-9]{2}[A-Z]{3}[0-9]{5}$")) {
            throw new ValidationException("Invalid Vietnamese student ID format");
        }
    }
    
    public void validateTeacherId(String teacherId) {
        // Vietnamese teacher ID format: GVDEPARTMENTSEQUENCE
        if (!teacherId.matches("^GV[A-Z]{4}[0-9]{4}$")) {
            throw new ValidationException("Invalid Vietnamese teacher ID format");
        }
    }
    
    public void validateAcademicStanding(Student student) {
        // Vietnamese academic standing rules
        double gpa = student.getCurrentGpa();
        int failedCredits = student.getFailedCredits();
        
        if (gpa < 2.0) {
            throw new ValidationException("GPA below minimum requirement (2.0)");
        }
        
        if (failedCredits > 12) {
            throw new ValidationException("Too many failed credits (max 12)");
        }
    }
    
    public void validateCourseLoad(Student student, Semester semester) {
        int currentCredits = getCurrentCredits(student, semester);
        int maxCredits = getMaxCreditsByLevel(student.getAcademicLevel());
        
        if (currentCredits > maxCredits) {
            throw new ValidationException("Course load exceeds maximum allowed");
        }
    }
    
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
}
```

## 4. API Design

### 4.1 Authentication APIs
```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @PostMapping("/register/student")
    public ResponseEntity<UserResponse> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        UserResponse response = tenantAwareAuthService.registerStudent(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register/teacher")
    public ResponseEntity<UserResponse> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        UserResponse response = tenantAwareAuthService.registerTeacher(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login/vietnamese-id")
    public ResponseEntity<UserResponse> loginByVietnameseId(@Valid @RequestBody VietnameseIdLoginRequest request) {
        UserResponse response = authService.loginByVietnameseId(request);
        return ResponseEntity.ok(response);
    }
}
```

### 4.2 Student APIs
```java
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    
    @GetMapping("/{vietnameseId}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable String vietnameseId) {
        Student student = studentService.findByVietnameseId(vietnameseId);
        return ResponseEntity.ok(StudentResponse.from(student));
    }
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByFaculty(@PathVariable Long facultyId) {
        List<Student> students = studentService.findByFaculty(facultyId);
        return ResponseEntity.ok(StudentResponse.fromList(students));
    }
    
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByClass(@PathVariable Long classId) {
        List<Student> students = studentService.findByClass(classId);
        return ResponseEntity.ok(StudentResponse.fromList(students));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(@RequestParam String query) {
        List<Student> students = studentService.search(query);
        return ResponseEntity.ok(StudentResponse.fromList(students));
    }
    
    @PutMapping("/{vietnameseId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> updateStudentStatus(
        @PathVariable String vietnameseId,
        @RequestBody UpdateStudentStatusRequest request) {
        Student student = studentService.updateStatus(vietnameseId, request.getStatus());
        return ResponseEntity.ok(StudentResponse.from(student));
    }
}
```

### 4.3 Academic APIs
```java
@RestController
@RequestMapping("/api/v1/academic")
public class AcademicController {
    
    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = academicService.enrollStudent(
            request.getStudentId(), 
            request.getCourseOfferingId()
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/enrollments/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(@PathVariable Long studentId) {
        List<Enrollment> enrollments = academicService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(EnrollmentResponse.fromList(enrollments));
    }
    
    @GetMapping("/enrollments/course-offering/{courseOfferingId}")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(@PathVariable Long courseOfferingId) {
        List<Enrollment> enrollments = academicService.getCourseEnrollments(courseOfferingId);
        return ResponseEntity.ok(EnrollmentResponse.fromList(enrollments));
    }
    
    @PostMapping("/enrollments/{enrollmentId}/grade")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<EnrollmentResponse> updateGrade(
        @PathVariable Long enrollmentId,
        @RequestBody GradeRequest request) {
        Enrollment enrollment = academicService.updateGrade(enrollmentId, request.getGrade());
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }
    
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(
        @RequestParam(required = false) Long facultyId,
        @RequestParam(required = false) Long departmentId) {
        List<Course> courses = courseService.findByFilters(facultyId, departmentId);
        return ResponseEntity.ok(CourseResponse.fromList(courses));
    }
    
    @GetMapping("/course-offerings")
    public ResponseEntity<List<CourseOfferingResponse>> getCourseOfferings(
        @RequestParam(required = false) Long semesterId,
        @RequestParam(required = false) Long teacherId) {
        List<CourseOffering> offerings = courseOfferingService.findByFilters(semesterId, teacherId);
        return ResponseEntity.ok(CourseOfferingResponse.fromList(offerings));
    }
}
```

## 5. Database Schema

### 5.1 Core Tables
```sql
-- Users table (enhanced)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    vietnamese_id VARCHAR(20) UNIQUE NOT NULL,
    id_category VARCHAR(20) NOT NULL CHECK (id_category IN ('SINHVIEN', 'GIAOVIEN', 'NHANVIEN')),
    academic_level VARCHAR(20) CHECK (academic_level IN ('DAIHOC', 'CAODANG', 'THACSI', 'TIENSI')),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    department_id BIGINT REFERENCES departments(id),
    class_id BIGINT REFERENCES class_groups(id),
    enrollment_year INTEGER,
    graduation_year INTEGER,
    password_hash VARCHAR(255) NOT NULL,
    system_role VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Academic structure
CREATE TABLE faculties (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE class_groups (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    major_id BIGINT REFERENCES majors(id),
    enrollment_year INTEGER NOT NULL,
    graduation_year INTEGER,
    max_students INTEGER DEFAULT 50,
    current_students INTEGER DEFAULT 0,
    advisor_id BIGINT REFERENCES teachers(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Course management
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    department_id BIGINT NOT NULL REFERENCES departments(id),
    credits INTEGER NOT NULL,
    theory_hours INTEGER,
    practice_hours INTEGER,
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('REQUIRED', 'ELECTIVE', 'OPTIONAL')),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_offerings (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    course_id BIGINT NOT NULL REFERENCES courses(id),
    semester_id BIGINT NOT NULL REFERENCES semesters(id),
    teacher_id BIGINT REFERENCES teachers(id),
    max_students INTEGER NOT NULL,
    current_students INTEGER DEFAULT 0,
    schedule JSONB,
    classroom VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'OPEN', 'CLOSED', 'CANCELLED')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id),
    course_offering_id BIGINT NOT NULL REFERENCES course_offerings(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED' CHECK (status IN ('ENROLLED', 'COMPLETED', 'FAILED', 'DROPPED')),
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    grade DECIMAL(5,2),
    letter_grade VARCHAR(2),
    gpa_points DECIMAL(3,2),
    attendance_rate DECIMAL(5,2),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, course_offering_id)
);

-- Student and Teacher specific tables
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    student_number VARCHAR(20) UNIQUE NOT NULL,
    class_id BIGINT REFERENCES class_groups(id),
    enrollment_date DATE NOT NULL,
    expected_graduation_date DATE,
    current_gpa DECIMAL(3,2),
    failed_credits INTEGER DEFAULT 0,
    academic_standing VARCHAR(20) DEFAULT 'GOOD' CHECK (academic_standing IN ('GOOD', 'PROBATION', 'SUSPENDED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teachers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    teacher_number VARCHAR(20) UNIQUE NOT NULL,
    department_id BIGINT NOT NULL REFERENCES departments(id),
    hire_date DATE NOT NULL,
    academic_rank VARCHAR(20),
    specialization VARCHAR(100),
    max_teaching_hours INTEGER DEFAULT 20,
    current_teaching_hours INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5.2 Indexes for Performance
```sql
-- User indexes
CREATE INDEX idx_users_vietnamese_id ON users(vietnamese_id);
CREATE INDEX idx_users_faculty_id ON users(faculty_id);
CREATE INDEX idx_users_class_id ON users(class_id);
CREATE INDEX idx_users_id_category ON users(id_category);
CREATE INDEX idx_users_email ON users(email);

-- Academic indexes
CREATE INDEX idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX idx_enrollments_course_offering_id ON enrollments(course_offering_id);
CREATE INDEX idx_enrollments_status ON enrollments(status);
CREATE INDEX idx_enrollments_semester_id ON enrollments(course_offering_id) WHERE course_offering_id IS NOT NULL;

CREATE INDEX idx_course_offerings_course_id ON course_offerings(course_id);
CREATE INDEX idx_course_offerings_semester_id ON course_offerings(semester_id);
CREATE INDEX idx_course_offerings_teacher_id ON course_offerings(teacher_id);
CREATE INDEX idx_course_offerings_status ON course_offerings(status);

CREATE INDEX idx_students_class_id ON students(class_id);
CREATE INDEX idx_students_academic_standing ON students(academic_standing);

CREATE INDEX idx_teachers_department_id ON teachers(department_id);
CREATE INDEX idx_teachers_is_active ON teachers(is_active);
```

### 5.3 Constraints and Triggers
```sql
-- Update timestamp trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply to relevant tables
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_faculties_updated_at BEFORE UPDATE ON faculties FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_departments_updated_at BEFORE UPDATE ON departments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_class_groups_updated_at BEFORE UPDATE ON class_groups FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_courses_updated_at BEFORE UPDATE ON courses FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_course_offerings_updated_at BEFORE UPDATE ON course_offerings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_enrollments_updated_at BEFORE UPDATE ON enrollments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Check constraints
ALTER TABLE users ADD CONSTRAINT chk_vietnamese_id_format 
    CHECK (vietnamese_id ~ '^(SV|GV|NV)[A-Z0-9]+$');

ALTER TABLE courses ADD CONSTRAINT chk_credits_positive 
    CHECK (credits > 0);

ALTER TABLE course_offerings ADD CONSTRAINT chk_max_students_positive 
    CHECK (max_students > 0);

ALTER TABLE enrollments ADD CONSTRAINT chk_grade_range 
    CHECK (grade IS NULL OR (grade >= 0 AND grade <= 10));

ALTER TABLE enrollments ADD CONSTRAINT chk_attendance_rate 
    CHECK (attendance_rate IS NULL OR (attendance_rate >= 0 AND attendance_rate <= 100));
```

## 6. Configuration Properties

### 6.1 Application Configuration
```yaml
# application.yml
educollege:
  vietnamese:
    id:
      student:
        format: "SV{year}{faculty}{sequence:5d}"
        sequence-start: 1
        year-format: "YY"
      teacher:
        format: "GV{department}{sequence:4d}"
        sequence-start: 1
      staff:
        format: "NV{faculty}{dept-type}{sequence:4d}"
        sequence-start: 1
      validation:
        student-pattern: "^SV[0-9]{2}[A-Z]{3}[0-9]{5}$"
        teacher-pattern: "^GV[A-Z]{4}[0-9]{4}$"
        staff-pattern: "^NV[A-Z]{3}[A-Z]{2}[0-9]{4}$"
    
    academic:
      max-credits-per-semester:
        DAIHOC: 25
        CAODANG: 20
        THACSI: 18
        TIENSI: 15
      max-courses-per-semester: 8
      enrollment-period-days: 14
      min-gpa-for-enrollment: 2.0
      max-failed-credits: 12
      attendance-requirement: 75.0
    
    grading:
      scale:
        A: 4.0
        B+: 3.5
        B: 3.0
        C+: 2.5
        C: 2.0
        D+: 1.5
        D: 1.0
        F: 0.0
      passing-grade: 4.0
      honors-gpa: 3.6
    
  tenant:
    default-faculty: "CNTT"
    isolation-level: "ROW"
    cache-enabled: true
    cache-ttl: 3600
    
  security:
    jwt:
      expiration: 86400000 # 24 hours
      refresh-expiration: 604800000 # 7 days
    password:
      min-length: 8
      require-uppercase: true
      require-lowercase: true
      require-digit: true
      require-special: true
    
  notification:
    email:
      enabled: true
      from: "noreply@educollege.edu.vn"
      templates-path: "templates/email/"
    sms:
      enabled: false
      provider: "viettel"
    
  storage:
    minio:
      bucket: "educollege-documents"
      region: "vn-south-1"
      secure: true
    
  cache:
    redis:
      ttl: 3600
      max-connections: 10
    
  messaging:
    rabbitmq:
      exchange: "educollege.events"
      queue: "academic.events"
```

### 6.2 Database Configuration
```yaml
spring:
  datasource:
    primary:
      url: jdbc:postgresql://localhost:5432/educollege_primary
      username: ${DB_USERNAME:educollege}
      password: ${DB_PASSWORD:password}
      hikari:
        maximum-pool-size: 20
        minimum-idle: 5
        connection-timeout: 30000
        idle-timeout: 600000
        max-lifetime: 1800000
    
    academic:
      url: jdbc:postgresql://localhost:5432/educollege_academic
      username: ${DB_USERNAME:educollege}
      password: ${DB_PASSWORD:password}
      hikari:
        maximum-pool-size: 15
        minimum-idle: 3
        connection-timeout: 30000
        idle-timeout: 600000
        max-lifetime: 1800000
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        use_sql_comments: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
  
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD:}
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
```

## 7. Implementation Roadmap

### 7.1 Phase 1: Core Structure (3-4 weeks)
**Week 1-2: Database & Entities**
- [ ] Update User entity với Vietnamese ID system
- [ ] Create Faculty, Department, ClassGroup entities
- [ ] Create database migration scripts
- [ ] Set up Vietnamese ID generation service

**Week 3-4: Authentication & Registration**
- [ ] Update authentication service với tenant context
- [ ] Implement student/teacher registration APIs
- [ ] Add Vietnamese ID validation
- [ ] Update JWT token generation

### 7.2 Phase 2: Academic Management (4-5 weeks)
**Week 5-6: Course Management**
- [ ] Create Course and CourseOffering entities
- [ ] Implement course management APIs
- [ ] Add Vietnamese course validation
- [ ] Create course catalog system

**Week 7-8: Enrollment System**
- [ ] Implement enrollment service
- [ ] Add academic validation rules
- [ ] Create enrollment APIs
- [ ] Add schedule conflict checking

**Week 9: Grade Management**
- [ ] Create grade tracking system
- [ ] Implement Vietnamese grading scale
- [ ] Add GPA calculation
- [ ] Create grade reporting

### 7.3 Phase 3: Advanced Features (3-4 weeks)
**Week 10-11: Schedule Management**
- [ ] Implement schedule system
- [ ] Add classroom management
- [ ] Create conflict detection
- [ ] Add timetable generation

**Week 12-13: Reporting & Analytics**
- [ ] Create academic reports
- [ ] Implement Vietnamese standard reports
- [ ] Add student performance analytics
- [ ] Create faculty workload reports

### 7.4 Phase 4: Integration & Testing (2-3 weeks)
**Week 14: Integration Testing**
- [ ] End-to-end testing
- [ ] Performance testing
- [ ] Load testing
- [ ] Security testing

**Week 15: Deployment & Documentation**
- [ ] Production deployment
- [ ] API documentation
- [ ] User documentation
- [ ] Admin guide

## 8. Testing Strategy

### 8.1 Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class VietnameseIdServiceTest {
    
    @Mock
    private SequenceRepository sequenceRepository;
    
    @InjectMocks
    private VietnameseIdService vietnameseIdService;
    
    @Test
    void shouldGenerateStudentIdCorrectly() {
        // Given
        Faculty faculty = Faculty.builder()
            .id(1L)
            .code("CNTT")
            .build();
            
        ClassGroup classGroup = ClassGroup.builder()
            .id(1L)
            .code("DH21CNTT01")
            .build();
        
        when(sequenceRepository.getNextStudentSequence(1L, 1L)).thenReturn(101);
        
        // When
        String studentId = vietnameseIdService.generateStudentId(faculty, classGroup, 2024);
        
        // Then
        assertEquals("SV24CNTT00101", studentId);
    }
    
    @Test
    void shouldValidateVietnameseIdFormat() {
        // Test valid formats
        assertTrue(vietnameseIdService.isValidStudentId("SV24CNTT00101"));
        assertTrue(vietnameseIdService.isValidTeacherId("GVCNPM0001"));
        
        // Test invalid formats
        assertFalse(vietnameseIdService.isValidStudentId("SV24CNTT001")); // Too short
        assertFalse(vietnameseIdService.isValidTeacherId("GVCNPM")); // Missing sequence
    }
}

@SpringBootTest
@Transactional
class AcademicServiceTest {
    
    @Autowired
    private AcademicService academicService;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void shouldEnrollStudentSuccessfully() {
        // Given
        Student student = createTestStudent();
        CourseOffering offering = createTestCourseOffering();
        
        // When
        EnrollmentResponse response = academicService.enrollStudent(student.getId(), offering.getId());
        
        // Then
        assertNotNull(response);
        assertEquals(EnrollmentStatus.ENROLLED, response.getStatus());
        assertEquals(1, offering.getCurrentStudents());
    }
    
    @Test
    void shouldRejectEnrollmentWhenCourseIsFull() {
        // Given
        Student student = createTestStudent();
        CourseOffering offering = createTestCourseOffering();
        offering.setMaxStudents(0);
        offering.setCurrentStudents(0);
        entityManager.persist(offering);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            academicService.enrollStudent(student.getId(), offering.getId());
        });
    }
}
```

### 8.2 Integration Tests
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AcademicControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldRegisterStudentSuccessfully() {
        // Given
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
            .email("student@educollege.edu.vn")
            .username("student123")
            .password("SecurePass123!")
            .confirmPassword("SecurePass123!")
            .facultyId(1L)
            .classId(1L)
            .enrollmentYear(2024)
            .build();
        
        // When
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
            "/api/v1/auth/register/student", request, UserResponse.class);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());
        assertEquals("SINHVIEN", response.getBody().getUser().getIdCategory());
        assertTrue(response.getBody().getUser().getVietnameseId().startsWith("SV24"));
    }
    
    @Test
    void shouldEnrollStudentInCourse() {
        // Given
        Long studentId = createTestStudent().getId();
        Long courseOfferingId = createTestCourseOffering().getId();
        
        EnrollmentRequest request = EnrollmentRequest.builder()
            .studentId(studentId)
            .courseOfferingId(courseOfferingId)
            .build();
        
        // When
        ResponseEntity<EnrollmentResponse> response = restTemplate.postForEntity(
            "/api/v1/academic/enrollments", request, EnrollmentResponse.class);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EnrollmentStatus.ENROLLED, response.getBody().getStatus());
    }
}
```

## 9. Security Considerations

### 9.1 Authentication & Authorization
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/academic/enrollments").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/academic/enrollments/*/grade").hasRole("TEACHER")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 9.2 Data Access Control
```java
@Component
public class VietnameseDataAccessControl {
    
    public boolean canAccessStudentData(User currentUser, Student targetStudent) {
        // Students can only access their own data
        if (currentUser.getIdCategory().equals("SINHVIEN")) {
            return currentUser.getId().equals(targetStudent.getUser().getId());
        }
        
        // Teachers can access students in their classes/departments
        if (currentUser.getIdCategory().equals("GIAOVIEN")) {
            return canTeacherAccessStudent(currentUser, targetStudent);
        }
        
        // Admins can access all data
        return currentUser.getSystemRole().equals(SystemRole.ADMIN);
    }
    
    public boolean canAccessCourseData(User currentUser, Course course) {
        // Students can access course information
        if (currentUser.getIdCategory().equals("SINHVIEN")) {
            return true;
        }
        
        // Teachers can access courses in their department
        if (currentUser.getIdCategory().equals("GIAOVIEN")) {
            return course.getDepartment().getFaculty().getId().equals(currentUser.getFaculty().getId());
        }
        
        return currentUser.getSystemRole().equals(SystemRole.ADMIN);
    }
    
    private boolean canTeacherAccessStudent(User teacher, Student student) {
        // Teacher can access students in their department
        if (teacher.getDepartment() != null && student.getUser().getDepartment() != null) {
            return teacher.getDepartment().getId().equals(student.getUser().getDepartment().getId());
        }
        
        // Teacher can access students in their faculty
        return teacher.getFaculty().getId().equals(student.getUser().getFaculty().getId());
    }
}
```

## 10. Performance Optimization

### 10.1 Caching Strategy
```java
@Service
public class VietnameseAcademicCacheService {
    
    @Cacheable(value = "students", key = "#vietnameseId")
    public Student getStudentByVietnameseId(String vietnameseId) {
        return studentRepository.findByVietnameseId(vietnameseId)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }
    
    @Cacheable(value = "courses", key = "#facultyId + '-' + #semesterId")
    public List<Course> getCoursesByFacultyAndSemester(Long facultyId, Long semesterId) {
        return courseRepository.findByFacultyAndSemester(facultyId, semesterId);
    }
    
    @CacheEvict(value = "enrollments", key = "#studentId")
    public void clearStudentEnrollmentCache(Long studentId) {
        // Cache eviction logic
    }
    
    @Cacheable(value = "academic-standings", key = "#studentId")
    public AcademicStanding getStudentAcademicStanding(Long studentId) {
        return calculateAcademicStanding(studentId);
    }
}
```

### 10.2 Database Optimization
```sql
-- Partitioning for large tables
CREATE TABLE enrollments_2024 PARTITION OF enrollments
FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE enrollments_2025 PARTITION OF enrollments
FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');

-- Materialized views for reporting
CREATE MATERIALIZED VIEW student_academic_summary AS
SELECT 
    s.id,
    s.vietnamese_id,
    s.full_name,
    f.name as faculty_name,
    c.code as class_code,
    COALESCE(AVG(e.grade), 0) as current_gpa,
    COUNT(e.id) as total_courses,
    SUM(CASE WHEN e.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_courses,
    SUM(COALESCE(co.credits, 0)) as total_credits
FROM users s
JOIN faculties f ON s.faculty_id = f.id
LEFT JOIN class_groups c ON s.class_id = c.id
LEFT JOIN enrollments e ON s.id = e.student_id
LEFT JOIN course_offerings co ON e.course_offering_id = co.id
LEFT JOIN courses cr ON co.course_id = cr.id
WHERE s.id_category = 'SINHVIEN'
GROUP BY s.id, s.vietnamese_id, s.full_name, f.name, c.code;

-- Refresh materialized view periodically
CREATE OR REPLACE FUNCTION refresh_student_summary()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY student_academic_summary;
END;
$$ LANGUAGE plpgsql;

-- Schedule refresh (requires pg_cron extension)
SELECT cron.schedule('refresh-student-summary', '0 2 * * *', 'SELECT refresh_student_summary();');
```

## 11. Monitoring & Logging

### 11.1 Application Monitoring
```java
@Component
public class VietnameseAcademicMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter studentRegistrationCounter;
    private final Counter courseEnrollmentCounter;
    private final Timer enrollmentProcessingTimer;
    
    public VietnameseAcademicMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.studentRegistrationCounter = Counter.builder("student.registration.count")
            .description("Total student registrations")
            .register(meterRegistry);
        this.courseEnrollmentCounter = Counter.builder("course.enrollment.count")
            .description("Total course enrollments")
            .register(meterRegistry);
        this.enrollmentProcessingTimer = Timer.builder("enrollment.processing.time")
            .description("Time taken to process enrollment")
            .register(meterRegistry);
    }
    
    public void recordStudentRegistration(String faculty) {
        studentRegistrationCounter.increment(Tags.of("faculty", faculty));
    }
    
    public void recordCourseEnrollment(String courseType) {
        courseEnrollmentCounter.increment(Tags.of("course_type", courseType));
    }
    
    public Timer.Sample startEnrollmentTimer() {
        return Timer.start(meterRegistry);
    }
}
```

### 11.2 Audit Logging
```java
@Component
@Slf4j
public class VietnameseAcademicAuditService {
    
    @EventListener
    public void handleStudentRegistration(StudentRegistrationEvent event) {
        log.info("Student registered: vietnameseId={}, faculty={}, class={}, timestamp={}",
            event.getVietnameseId(),
            event.getFacultyCode(),
            event.getClassCode(),
            event.getTimestamp());
        
        // Store in audit table
        auditRepository.save(AuditLog.builder()
            .action("STUDENT_REGISTRATION")
            .entityId(event.getStudentId())
            .details(String.format("Vietnamese ID: %s, Faculty: %s, Class: %s",
                event.getVietnameseId(), event.getFacultyCode(), event.getClassCode()))
            .timestamp(event.getTimestamp())
            .build());
    }
    
    @EventListener
    public void handleCourseEnrollment(CourseEnrollmentEvent event) {
        log.info("Course enrollment: studentId={}, courseOfferingId={}, timestamp={}",
            event.getStudentId(),
            event.getCourseOfferingId(),
            event.getTimestamp());
        
        auditRepository.save(AuditLog.builder()
            .action("COURSE_ENROLLMENT")
            .entityId(event.getEnrollmentId())
            .details(String.format("Student: %s, Course: %s",
                event.getStudentVietnameseId(), event.getCourseCode()))
            .timestamp(event.getTimestamp())
            .build());
    }
    
    @EventListener
    public void handleGradeUpdate(GradeUpdateEvent event) {
        log.info("Grade updated: enrollmentId={}, grade={}, updatedBy={}, timestamp={}",
            event.getEnrollmentId(),
            event.getGrade(),
            event.getUpdatedBy(),
            event.getTimestamp());
        
        auditRepository.save(AuditLog.builder()
            .action("GRADE_UPDATE")
            .entityId(event.getEnrollmentId())
            .details(String.format("Grade: %s, Updated by: %s",
                event.getGrade(), event.getUpdatedBy()))
            .timestamp(event.getTimestamp())
            .build());
    }
}
```

## 12. Deployment & Operations

### 12.1 Docker Configuration
```dockerfile
# Dockerfile
FROM openjdk:21-jre-slim

LABEL maintainer="eduCollege Team"
LABEL version="1.0.0"

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create app user
RUN groupadd -r educollege && useradd -r -g educollege educollege

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/educollege-*.jar app.jar

# Change ownership
RUN chown -R educollege:educollege /app

# Switch to non-root user
USER educollege

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "app.jar"]
```

### 12.2 Docker Compose
```yaml
# docker-compose.yml
version: '3.8'

services:
  educollege-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DB_USERNAME=educollege
      - DB_PASSWORD=${DB_PASSWORD}
      - REDIS_PASSWORD=${REDIS_PASSWORD}
    depends_on:
      - postgres-primary
      - postgres-academic
      - redis
    networks:
      - educollege-network
    restart: unless-stopped
  
  postgres-primary:
    image: postgres:15
    environment:
      - POSTGRES_DB=educollege_primary
      - POSTGRES_USER=educollege
      - POSTGRES_PASSWORD=${DB_PASSWORD}
    volumes:
      - postgres-primary-data:/var/lib/postgresql/data
      - ./scripts/database/migrations:/docker-entrypoint-initdb.d
    networks:
      - educollege-network
    restart: unless-stopped
  
  postgres-academic:
    image: postgres:15
    environment:
      - POSTGRES_DB=educollege_academic
      - POSTGRES_USER=educollege
      - POSTGRES_PASSWORD=${DB_PASSWORD}
    volumes:
      - postgres-academic-data:/var/lib/postgresql/data
    networks:
      - educollege-network
    restart: unless-stopped
  
  redis:
    image: redis:7-alpine
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis-data:/data
    networks:
      - educollege-network
    restart: unless-stopped
  
  minio:
    image: minio/minio:latest
    command: server /data --console-address ":9001"
    environment:
      - MINIO_ROOT_USER=${MINIO_ROOT_USER}
      - MINIO_ROOT_PASSWORD=${MINIO_ROOT_PASSWORD}
    volumes:
      - minio-data:/data
    ports:
      - "9000:9000"
      - "9001:9001"
    networks:
      - educollege-network
    restart: unless-stopped

volumes:
  postgres-primary-data:
  postgres-academic-data:
  redis-data:
  minio-data:

networks:
  educollege-network:
    driver: bridge
```

### 12.3 Kubernetes Deployment
```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: educollege-app
  labels:
    app: educollege
spec:
  replicas: 3
  selector:
    matchLabels:
      app: educollege
  template:
    metadata:
      labels:
        app: educollege
    spec:
      containers:
      - name: educollege
        image: educollege:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DB_USERNAME
          value: "educollege"
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: educollege-secrets
              key: db-password
        - name: REDIS_PASSWORD
          valueFrom:
            secretKeyRef:
              name: educollege-secrets
              key: redis-password
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10

---
apiVersion: v1
kind: Service
metadata:
  name: educollege-service
spec:
  selector:
    app: educollege
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

## 13. Conclusion

Architecture này được thiết kế dựa trên best practices từ các trường đại học hàng đầu Việt Nam, kết hợp với modern software architecture patterns. Các đặc điểm chính:

### 13.1 Strengths
- **Vietnamese Context**: ID system và validation theo chuẩn Việt Nam
- **Scalability**: Microservices architecture với tenant-based design
- **Flexibility**: Support multiple academic levels và institutional types
- **Integration**: Tận dụng existing eduCollege architecture
- **Performance**: Caching strategies và database optimization
- **Security**: Role-based access control với Vietnamese data protection

### 13.2 Next Steps
1. **Implement Phase 1** với core Vietnamese ID system
2. **Gather feedback** từ real Vietnamese universities
3. **Iterate and improve** based on usage patterns
4. **Scale to multiple institutions** với multi-tenant support

Architecture này cung cấp foundation vững chắc cho một hệ thống quản lý đại học chuyên nghiệp tại Việt Nam.
