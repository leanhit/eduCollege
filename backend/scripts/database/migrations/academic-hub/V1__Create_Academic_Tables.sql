-- Create Academic Tables for eduCollege Vietnamese University System

-- Create Faculties table
CREATE TABLE IF NOT EXISTS faculties (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    description TEXT,
    dean_name VARCHAR(100),
    dean_email VARCHAR(255),
    office_location VARCHAR(200),
    phone_number VARCHAR(20),
    website VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Departments table
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    description TEXT,
    head_name VARCHAR(100),
    head_email VARCHAR(255),
    office_location VARCHAR(200),
    phone_number VARCHAR(20),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Majors table
CREATE TABLE IF NOT EXISTS majors (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    description TEXT,
    degree_level VARCHAR(20),
    duration_years INTEGER,
    credits_required INTEGER,
    accreditation VARCHAR(100),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Class Groups table
CREATE TABLE IF NOT EXISTS class_groups (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    faculty_id BIGINT NOT NULL REFERENCES faculties(id) ON DELETE CASCADE,
    major_id BIGINT REFERENCES majors(id) ON DELETE SET NULL,
    enrollment_year INTEGER NOT NULL,
    graduation_year INTEGER,
    max_students INTEGER DEFAULT 50,
    current_students INTEGER DEFAULT 0,
    advisor_id BIGINT REFERENCES teachers(id) ON DELETE SET NULL,
    classroom VARCHAR(50),
    schedule TEXT,
    description TEXT,
    class_type VARCHAR(20) DEFAULT 'REGULAR',
    study_program VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Teachers table
CREATE TABLE IF NOT EXISTS teachers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    teacher_number VARCHAR(20) UNIQUE NOT NULL,
    department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE,
    hire_date DATE NOT NULL,
    academic_rank VARCHAR(50),
    highest_degree VARCHAR(50),
    specialization VARCHAR(200),
    research_interests TEXT,
    office_location VARCHAR(200),
    office_hours VARCHAR(100),
    phone_number VARCHAR(20),
    personal_email VARCHAR(255),
    max_teaching_hours INTEGER DEFAULT 20,
    current_teaching_hours INTEGER DEFAULT 0,
    max_advising_students INTEGER DEFAULT 15,
    current_advising_students INTEGER DEFAULT 0,
    contract_type VARCHAR(50),
    salary_scale VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    termination_date DATE,
    termination_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Students table
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    class_id BIGINT REFERENCES class_groups(id) ON DELETE SET NULL,
    enrollment_date DATE NOT NULL,
    expected_graduation_date DATE,
    actual_graduation_date DATE,
    current_gpa DECIMAL(3,2),
    cumulative_gpa DECIMAL(3,2),
    failed_credits INTEGER DEFAULT 0,
    completed_credits INTEGER DEFAULT 0,
    total_credits INTEGER DEFAULT 0,
    academic_standing VARCHAR(20) DEFAULT 'GOOD',
    student_status VARCHAR(20) DEFAULT 'ENROLLED',
    study_mode VARCHAR(50),
    funding_type VARCHAR(50),
    scholarship_info TEXT,
    notes TEXT,
    last_semester_gpa DECIMAL(3,2),
    warning_count INTEGER DEFAULT 0,
    probation_start_date DATE,
    probation_end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_faculties_code ON faculties(code);
CREATE INDEX IF NOT EXISTS idx_faculties_is_active ON faculties(is_active);

CREATE INDEX IF NOT EXISTS idx_departments_code ON departments(code);
CREATE INDEX IF NOT EXISTS idx_departments_faculty_id ON departments(faculty_id);
CREATE INDEX IF NOT EXISTS idx_departments_is_active ON departments(is_active);

CREATE INDEX IF NOT EXISTS idx_majors_code ON majors(code);
CREATE INDEX IF NOT EXISTS idx_majors_faculty_id ON majors(faculty_id);
CREATE INDEX IF NOT EXISTS idx_majors_is_active ON majors(is_active);

CREATE INDEX IF NOT EXISTS idx_class_groups_code ON class_groups(code);
CREATE INDEX IF NOT EXISTS idx_class_groups_faculty_id ON class_groups(faculty_id);
CREATE INDEX IF NOT EXISTS idx_class_groups_major_id ON class_groups(major_id);
CREATE INDEX IF NOT EXISTS idx_class_groups_enrollment_year ON class_groups(enrollment_year);
CREATE INDEX IF NOT EXISTS idx_class_groups_is_active ON class_groups(is_active);

CREATE INDEX IF NOT EXISTS idx_teachers_user_id ON teachers(user_id);
CREATE INDEX IF NOT EXISTS idx_teachers_teacher_number ON teachers(teacher_number);
CREATE INDEX IF NOT EXISTS idx_teachers_department_id ON teachers(department_id);
CREATE INDEX IF NOT EXISTS idx_teachers_is_active ON teachers(is_active);

CREATE INDEX IF NOT EXISTS idx_students_user_id ON students(user_id);
CREATE INDEX IF NOT EXISTS idx_students_student_number ON students(student_number);
CREATE INDEX IF NOT EXISTS idx_students_class_id ON students(class_id);
CREATE INDEX IF NOT EXISTS idx_students_academic_standing ON students(academic_standing);
CREATE INDEX IF NOT EXISTS idx_students_student_status ON students(student_status);

-- Create triggers for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to all tables
CREATE TRIGGER update_faculties_updated_at BEFORE UPDATE ON faculties FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_departments_updated_at BEFORE UPDATE ON departments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_majors_updated_at BEFORE UPDATE ON majors FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_class_groups_updated_at BEFORE UPDATE ON class_groups FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_teachers_updated_at BEFORE UPDATE ON teachers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_students_updated_at BEFORE UPDATE ON students FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Add constraints
ALTER TABLE faculties ADD CONSTRAINT chk_faculties_code_format 
    CHECK (code ~ '^[A-Z0-9]+$');

ALTER TABLE departments ADD CONSTRAINT chk_departments_code_format 
    CHECK (code ~ '^[A-Z0-9]+$');

ALTER TABLE majors ADD CONSTRAINT chk_majors_code_format 
    CHECK (code ~ '^[A-Z0-9]+$');

ALTER TABLE class_groups ADD CONSTRAINT chk_class_groups_code_format 
    CHECK (code ~ '^[A-Z0-9]+$');

ALTER TABLE class_groups ADD CONSTRAINT chk_class_groups_year 
    CHECK (enrollment_year >= 2000 AND enrollment_year <= 2100);

ALTER TABLE class_groups ADD CONSTRAINT chk_class_groups_students 
    CHECK (max_students > 0 AND current_students >= 0 AND current_students <= max_students);

ALTER TABLE teachers ADD CONSTRAINT chk_teachers_hours 
    CHECK (max_teaching_hours > 0 AND current_teaching_hours >= 0 AND current_teaching_hours <= max_teaching_hours);

ALTER TABLE teachers ADD CONSTRAINT chk_teachers_advising 
    CHECK (max_advising_students > 0 AND current_advising_students >= 0 AND current_advising_students <= max_advising_students);

ALTER TABLE students ADD CONSTRAINT chk_students_gpa 
    CHECK (current_gpa IS NULL OR (current_gpa >= 0 AND current_gpa <= 4.0));

ALTER TABLE students ADD CONSTRAINT chk_students_credits 
    CHECK (failed_credits >= 0 AND completed_credits >= 0 AND total_credits >= 0);

ALTER TABLE students ADD CONSTRAINT chk_students_academic_standing 
    CHECK (academic_standing IN ('GOOD', 'PROBATION', 'SUSPENDED'));

ALTER TABLE students ADD CONSTRAINT chk_students_status 
    CHECK (student_status IN ('ENROLLED', 'GRADUATED', 'DROPPED', 'SUSPENDED', 'TRANSFERRED'));

-- Insert default data
INSERT INTO faculties (code, name, vietnamese_name, english_name, description) VALUES
('CNTT', 'Faculty of Information Technology', 'Khoa Công nghệ Thông tin', 'Faculty of Information Technology', 'Khoa đào tạo ngành Công nghệ Thông tin'),
('TOAN', 'Faculty of Mathematics', 'Khoa Toán', 'Faculty of Mathematics', 'Khoa đào tạo ngành Toán học'),
('LY', 'Faculty of Physics', 'Khoa Vật lý', 'Faculty of Physics', 'Khoa đào tạo ngành Vật lý'),
('HOA', 'Faculty of Chemistry', 'Khoa Hóa học', 'Faculty of Chemistry', 'Khoa đào tạo ngành Hóa học')
ON CONFLICT (code) DO NOTHING;

-- Insert default departments for CNTT faculty
INSERT INTO departments (code, name, vietnamese_name, english_name, faculty_id) VALUES
('CNPM', 'Software Engineering', 'Bộ môn Công nghệ Phần mềm', 'Software Engineering', 1),
('MMTT', 'Computer Networks', 'Bộ môn Mạng và Truyền thông', 'Computer Networks', 1),
('HTTT', 'Information Systems', 'Bộ môn Hệ thống Thông tin', 'Information Systems', 1),
('TRITU', 'Artificial Intelligence', 'Bộ môn Trí tuệ Nhân tạo', 'Artificial Intelligence', 1)
ON CONFLICT (code) DO NOTHING;

-- Insert default majors
INSERT INTO majors (code, name, vietnamese_name, english_name, degree_level, duration_years, credits_required, faculty_id) VALUES
('CNTT', 'Information Technology', 'Công nghệ Thông tin', 'Information Technology', 'DAIHOC', 4, 150, 1),
('KTPM', 'Software Engineering', 'Kỹ thuật Phần mềm', 'Software Engineering', 'DAIHOC', 4, 150, 1),
('KHMT', 'Computer Science', 'Khoa học Máy tính', 'Computer Science', 'DAIHOC', 4, 150, 1),
('TTNT', 'Artificial Intelligence', 'Trí tuệ Nhân tạo', 'Artificial Intelligence', 'DAIHOC', 4, 150, 1)
ON CONFLICT (code) DO NOTHING;
