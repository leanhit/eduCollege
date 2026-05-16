-- EduCollege Database Migration Script
-- Version 1.0
-- Creates core tables with Vietnamese academic system support

-- Users table (enhanced with Vietnamese ID system)
CREATE TABLE IF NOT EXISTS users (
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
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    student_status VARCHAR(20) CHECK (student_status IN ('ENROLLED', 'GRADUATED', 'DROPPED')),
    is_active BOOLEAN DEFAULT TRUE,
    is_locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Academic structure
CREATE TABLE IF NOT EXISTS faculties (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    description TEXT,
    contact_phone VARCHAR(20),
    contact_email VARCHAR(100),
    office_location VARCHAR(200),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
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

CREATE TABLE IF NOT EXISTS majors (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS class_groups (
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
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    department_id BIGINT NOT NULL REFERENCES departments(id),
    credits INTEGER NOT NULL CHECK (credits > 0),
    theory_hours INTEGER,
    practice_hours INTEGER,
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('REQUIRED', 'ELECTIVE', 'OPTIONAL')),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS semesters (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    vietnamese_name VARCHAR(100) NOT NULL,
    academic_year INTEGER NOT NULL,
    semester_number INTEGER NOT NULL CHECK (semester_number BETWEEN 1 AND 3),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    registration_start DATE NOT NULL,
    registration_end DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course_offerings (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    course_id BIGINT NOT NULL REFERENCES courses(id),
    semester_id BIGINT NOT NULL REFERENCES semesters(id),
    teacher_id BIGINT REFERENCES teachers(id),
    max_students INTEGER NOT NULL CHECK (max_students > 0),
    current_students INTEGER DEFAULT 0,
    schedule JSONB,
    classroom VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'OPEN', 'CLOSED', 'CANCELLED')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Student and Teacher specific tables
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    student_number VARCHAR(20) UNIQUE NOT NULL,
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    class_id BIGINT REFERENCES class_groups(id),
    enrollment_date DATE NOT NULL,
    expected_graduation_date DATE,
    enrollment_year INTEGER,
    graduation_year INTEGER,
    current_gpa DECIMAL(3,2) DEFAULT 0.0 CHECK (current_gpa BETWEEN 0 AND 4.0),
    cumulative_gpa DECIMAL(3,2) DEFAULT 0.0 CHECK (cumulative_gpa BETWEEN 0 AND 4.0),
    total_credits INTEGER DEFAULT 0,
    completed_credits INTEGER DEFAULT 0,
    failed_credits INTEGER DEFAULT 0,
    academic_standing VARCHAR(20) DEFAULT 'GOOD' CHECK (academic_standing IN ('GOOD', 'PROBATION', 'SUSPENDED')),
    student_status VARCHAR(20) DEFAULT 'ENROLLED' CHECK (student_status IN ('ENROLLED', 'GRADUATED', 'DROPPED')),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS teachers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    teacher_number VARCHAR(20) UNIQUE NOT NULL,
    department_id BIGINT NOT NULL REFERENCES departments(id),
    academic_title VARCHAR(50),
    hire_date DATE NOT NULL,
    specialization VARCHAR(100),
    email VARCHAR(100),
    max_teaching_hours INTEGER DEFAULT 20,
    current_teaching_hours INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_advisor BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollment management
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id),
    course_offering_id BIGINT NOT NULL REFERENCES course_offerings(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED' CHECK (status IN ('ENROLLED', 'COMPLETED', 'FAILED', 'DROPPED')),
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    grade DECIMAL(5,2) CHECK (grade IS NULL OR (grade >= 0 AND grade <= 10)),
    letter_grade VARCHAR(2),
    gpa_points DECIMAL(3,2),
    attendance_rate DECIMAL(5,2) CHECK (attendance_rate IS NULL OR (attendance_rate >= 0 AND attendance_rate <= 100)),
    midterm_grade DECIMAL(5,2),
    final_grade DECIMAL(5,2),
    assignment_grade DECIMAL(5,2),
    participation_grade DECIMAL(5,2),
    grade_breakdown JSONB,
    graded_by BIGINT,
    grade_submission_date TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, course_offering_id)
);

-- Sequence management for Vietnamese ID generation
CREATE TABLE IF NOT EXISTS sequences (
    id BIGSERIAL PRIMARY KEY,
    sequence_key VARCHAR(100) UNIQUE NOT NULL,
    current_value BIGINT DEFAULT 0,
    faculty_id BIGINT REFERENCES faculties(id),
    department_id BIGINT REFERENCES departments(id),
    class_group_id BIGINT REFERENCES class_groups(id),
    year INTEGER,
    sequence_type VARCHAR(20) NOT NULL CHECK (sequence_type IN ('STUDENT', 'TEACHER', 'STAFF')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Advising system
CREATE TABLE IF NOT EXISTS advising_sessions (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id),
    teacher_id BIGINT NOT NULL REFERENCES teachers(id),
    semester_id BIGINT REFERENCES semesters(id),
    session_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    notes TEXT,
    recommendations TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Finance Management
CREATE TABLE IF NOT EXISTS tuition_fees (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id),
    semester_id BIGINT NOT NULL REFERENCES semesters(id),
    total_amount DECIMAL(12,2) NOT NULL,
    paid_amount DECIMAL(12,2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' CHECK (status IN ('UNPAID', 'PARTIAL', 'PAID', 'OVERDUE')),
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payment_transactions (
    id BIGSERIAL PRIMARY KEY,
    tuition_fee_id BIGINT NOT NULL REFERENCES tuition_fees(id),
    amount DECIMAL(12,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50),
    reference_number VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('SUCCESS', 'FAILED', 'PENDING')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
