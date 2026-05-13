-- Create Course Management Tables for Vietnamese University System

-- Create Courses table
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(200),
    department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE,
    credits INTEGER NOT NULL CHECK (credits > 0),
    theory_hours INTEGER,
    practice_hours INTEGER,
    lab_hours INTEGER,
    self_study_hours INTEGER,
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('REQUIRED', 'ELECTIVE', 'OPTIONAL')),
    course_level VARCHAR(10),
    prerequisite_courses TEXT,
    corequisite_courses TEXT,
    description TEXT,
    objectives TEXT,
    learning_outcomes TEXT,
    teaching_methods TEXT,
    assessment_methods TEXT,
    textbook VARCHAR(500),
    reference_materials TEXT,
    language_of_instruction VARCHAR(50) DEFAULT 'VIETNAMESE',
    min_students INTEGER DEFAULT 5 CHECK (min_students > 0),
    max_students INTEGER DEFAULT 200 CHECK (max_students > 0 AND max_students >= min_students),
    difficulty_level VARCHAR(20),
    accredited_until TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Semesters table
CREATE TABLE IF NOT EXISTS semesters (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    vietnamese_name VARCHAR(200) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    semester_type VARCHAR(20) NOT NULL,
    semester_number INTEGER NOT NULL CHECK (semester_number > 0 AND semester_number <= 4),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    enrollment_start_date DATE NOT NULL,
    enrollment_end_date DATE NOT NULL,
    withdrawal_deadline DATE,
    grade_submission_deadline DATE,
    final_exam_start_date DATE,
    final_exam_end_date DATE,
    break_start_date DATE,
    break_end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Course Offerings table
CREATE TABLE IF NOT EXISTS course_offerings (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    semester_id BIGINT NOT NULL REFERENCES semesters(id) ON DELETE CASCADE,
    teacher_id BIGINT REFERENCES teachers(id) ON DELETE SET NULL,
    assistant_teacher_id BIGINT REFERENCES teachers(id) ON DELETE SET NULL,
    max_students INTEGER NOT NULL CHECK (max_students > 0),
    current_students INTEGER DEFAULT 0 CHECK (current_students >= 0),
    min_students INTEGER DEFAULT 5 CHECK (min_students > 0),
    waitlist_capacity INTEGER DEFAULT 10 CHECK (waitlist_capacity >= 0),
    current_waitlist INTEGER DEFAULT 0 CHECK (current_waitlist >= 0),
    classroom VARCHAR(50),
    building VARCHAR(50),
    schedule TEXT,
    meeting_pattern VARCHAR(100),
    meeting_days VARCHAR(50),
    start_time TIME,
    end_time TIME,
    final_exam_date DATE,
    final_exam_time TIME,
    final_exam_room VARCHAR(50),
    midterm_exam_date DATE,
    midterm_exam_time TIME,
    midterm_exam_room VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'OPEN', 'CLOSED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    delivery_method VARCHAR(20) DEFAULT 'IN_PERSON',
    instructional_language VARCHAR(50) DEFAULT 'VIETNAMESE',
    notes TEXT,
    special_requirements TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_full BOOLEAN DEFAULT FALSE,
    has_conflict BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Create Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_offering_id BIGINT NOT NULL REFERENCES course_offerings(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED' CHECK (status IN ('ENROLLED', 'ATTENDING', 'WITHDRAWN', 'COMPLETED', 'FAILED', 'INCOMPLETE', 'AUDITING')),
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    withdrawal_date TIMESTAMP,
    withdrawal_reason TEXT,
    
    -- Grading information
    midterm_grade DECIMAL(5,2) CHECK (midterm_grade IS NULL OR (midterm_grade >= 0 AND midterm_grade <= 10)),
    final_grade DECIMAL(5,2) CHECK (final_grade IS NULL OR (final_grade >= 0 AND final_grade <= 10)),
    assignment_grade DECIMAL(5,2) CHECK (assignment_grade IS NULL OR (assignment_grade >= 0 AND assignment_grade <= 10)),
    lab_grade DECIMAL(5,2) CHECK (lab_grade IS NULL OR (lab_grade >= 0 AND lab_grade <= 10)),
    participation_grade DECIMAL(5,2) CHECK (participation_grade IS NULL OR (participation_grade >= 0 AND participation_grade <= 10)),
    final_numeric_grade DECIMAL(5,2) CHECK (final_numeric_grade IS NULL OR (final_numeric_grade >= 0 AND final_numeric_grade <= 10)),
    final_letter_grade VARCHAR(2),
    gpa_points DECIMAL(3,2) CHECK (gpa_points IS NULL OR (gpa_points >= 0 AND gpa_points <= 4.0)),
    grade_points DECIMAL(8,2),
    is_passed BOOLEAN,
    credits_earned INTEGER,
    
    -- Attendance information
    total_sessions INTEGER,
    attended_sessions INTEGER,
    attendance_rate DECIMAL(5,2) CHECK (attendance_rate IS NULL OR (attendance_rate >= 0 AND attendance_rate <= 100)),
    absence_count INTEGER DEFAULT 0,
    excused_absence_count INTEGER DEFAULT 0,
    
    -- Additional information
    notes TEXT,
    special_circumstances TEXT,
    academic_standing_at_enrollment VARCHAR(20),
    is_retake BOOLEAN DEFAULT FALSE,
    retake_count INTEGER DEFAULT 0,
    original_enrollment_id BIGINT REFERENCES enrollments(id) ON DELETE SET NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    
    UNIQUE(student_id, course_offering_id)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_courses_code ON courses(code);
CREATE INDEX IF NOT EXISTS idx_courses_department_id ON courses(department_id);
CREATE INDEX IF NOT EXISTS idx_courses_course_type ON courses(course_type);
CREATE INDEX IF NOT EXISTS idx_courses_is_active ON courses(is_active);

CREATE INDEX IF NOT EXISTS idx_semesters_code ON semesters(code);
CREATE INDEX IF NOT EXISTS idx_semesters_academic_year ON semesters(academic_year);
CREATE INDEX IF NOT EXISTS idx_semesters_is_current ON semesters(is_current);
CREATE INDEX IF NOT EXISTS idx_semesters_is_active ON semesters(is_active);

CREATE INDEX IF NOT EXISTS idx_course_offerings_code ON course_offerings(code);
CREATE INDEX IF NOT EXISTS idx_course_offerings_course_id ON course_offerings(course_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_semester_id ON course_offerings(semester_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_teacher_id ON course_offerings(teacher_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_status ON course_offerings(status);
CREATE INDEX IF NOT EXISTS idx_course_offerings_is_active ON course_offerings(is_active);

CREATE INDEX IF NOT EXISTS idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course_offering_id ON enrollments(course_offering_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_status ON enrollments(status);
CREATE INDEX IF NOT EXISTS idx_enrollments_enrollment_date ON enrollments(enrollment_date);
CREATE INDEX IF NOT EXISTS idx_enrollments_is_retake ON enrollments(is_retake);

-- Create triggers for updated_at
CREATE TRIGGER update_courses_updated_at BEFORE UPDATE ON courses FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_semesters_updated_at BEFORE UPDATE ON semesters FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_course_offerings_updated_at BEFORE UPDATE ON course_offerings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_enrollments_updated_at BEFORE UPDATE ON enrollments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Add constraints
ALTER TABLE courses ADD CONSTRAINT chk_courses_code_format 
    CHECK (code ~ '^[A-Z0-9]+$');

ALTER TABLE semesters ADD CONSTRAINT chk_semesters_dates 
    CHECK (end_date > start_date AND enrollment_end_date > enrollment_start_date);

ALTER TABLE course_offerings ADD CONSTRAINT chk_course_offerings_students 
    CHECK (max_students > min_students AND current_students <= max_students AND current_waitlist <= waitlist_capacity);

ALTER TABLE course_offerings ADD CONSTRAINT chk_course_offerings_times 
    CHECK (start_time < end_time);

ALTER TABLE enrollments ADD CONSTRAINT chk_enrollments_grades 
    CHECK (final_numeric_grade IS NULL OR (final_numeric_grade >= 0 AND final_numeric_grade <= 10));

-- Create functions for academic calculations
CREATE OR REPLACE FUNCTION calculate_gpa_points(numeric_grade DECIMAL, credits INTEGER)
RETURNS DECIMAL AS $$
BEGIN
    IF numeric_grade IS NULL OR credits IS NULL THEN
        RETURN NULL;
    END IF;
    
    RETURN CASE
        WHEN numeric_grade >= 8.5 THEN numeric_grade * credits; -- A = 4.0
        WHEN numeric_grade >= 8.0 THEN 3.5 * credits; -- B+ = 3.5
        WHEN numeric_grade >= 7.0 THEN 3.0 * credits; -- B = 3.0
        WHEN numeric_grade >= 6.5 THEN 2.5 * credits; -- C+ = 2.5
        WHEN numeric_grade >= 5.5 THEN 2.0 * credits; -- C = 2.0
        WHEN numeric_grade >= 5.0 THEN 1.5 * credits; -- D+ = 1.5
        WHEN numeric_grade >= 4.0 THEN 1.0 * credits; -- D = 1.0
        ELSE 0.0 * credits; -- F = 0.0
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_letter_grade(numeric_grade DECIMAL)
RETURNS VARCHAR(2) AS $$
BEGIN
    IF numeric_grade IS NULL THEN
        RETURN NULL;
    END IF;
    
    RETURN CASE
        WHEN numeric_grade >= 8.5 THEN 'A';
        WHEN numeric_grade >= 8.0 THEN 'B+';
        WHEN numeric_grade >= 7.0 THEN 'B';
        WHEN numeric_grade >= 6.5 THEN 'C+';
        WHEN numeric_grade >= 5.5 THEN 'C';
        WHEN numeric_grade >= 5.0 THEN 'D+';
        WHEN numeric_grade >= 4.0 THEN 'D';
        ELSE 'F';
    END;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for automatic grade calculation
CREATE OR REPLACE FUNCTION calculate_enrollment_grades()
RETURNS TRIGGER AS $$
BEGIN
    -- Calculate final numeric grade if components are provided
    IF NEW.final_numeric_grade IS NULL AND (
        NEW.midterm_grade IS NOT NULL OR 
        NEW.final_grade IS NOT NULL OR 
        NEW.assignment_grade IS NOT NULL OR 
        NEW.participation_grade IS NOT NULL
    ) THEN
        NEW.final_numeric_grade = (
            COALESCE(NEW.midterm_grade, 0) * 0.3 +
            COALESCE(NEW.final_grade, 0) * 0.5 +
            COALESCE(NEW.assignment_grade, 0) * 0.15 +
            COALESCE(NEW.participation_grade, 0) * 0.05
        );
    END IF;
    
    -- Calculate letter grade
    NEW.final_letter_grade = calculate_letter_grade(NEW.final_numeric_grade);
    
    -- Calculate GPA points
    IF NEW.final_numeric_grade IS NOT NULL THEN
        SELECT c.credits INTO NEW.credits_earned
        FROM course_offerings co
        JOIN courses c ON co.id = NEW.course_offering_id
        WHERE co.id = NEW.course_offering_id;
        
        NEW.gpa_points = calculate_gpa_points(NEW.final_numeric_grade, NEW.credits_earned);
        NEW.grade_points = NEW.gpa_points;
        
        -- Determine if passed
        NEW.is_passed = NEW.final_numeric_grade >= 4.0;
        
        -- Set credits earned based on passing status
        IF NOT NEW.is_passed THEN
            NEW.credits_earned = 0;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calculate_enrollment_grades
    BEFORE INSERT OR UPDATE ON enrollments
    FOR EACH ROW
    EXECUTE FUNCTION calculate_enrollment_grades();

-- Insert default data
INSERT INTO semesters (code, name, vietnamese_name, academic_year, semester_type, semester_number, 
    start_date, end_date, enrollment_start_date, enrollment_end_date, withdrawal_deadline, grade_submission_deadline) VALUES
('20241', 'Học kỳ 1 năm 2024', 'Học kỳ 1 năm 2024', '2024-2025', 'FALL', 1,
    '2024-09-01', '2025-01-15', '2024-08-15', '2024-09-15', '2024-11-01', '2025-01-25'),
('20242', 'Học kỳ 2 năm 2024', 'Học kỳ 2 năm 2024', '2024-2025', 'SPRING', 2,
    '2025-01-20', '2025-06-15', '2025-01-10', '2025-01-25', '2025-03-01', '2025-06-25'),
('20243', 'Học kỳ hè năm 2024', 'Học kỳ hè năm 2024', '2024-2025', 'SUMMER', 3,
    '2025-06-20', '2025-08-20', '2025-06-10', '2025-06-25', '2025-07-15', '2025-08-25')
ON CONFLICT (code) DO NOTHING;

-- Set current semester
UPDATE semesters SET is_current = true WHERE code = '20241';

-- Insert sample courses
INSERT INTO courses (code, name, vietnamese_name, department_id, credits, theory_hours, practice_hours, course_type, course_level, description) VALUES
('TIN101', 'Programming Fundamentals', 'Lập trình C cơ bản', 1, 3, 2, 2, 'REQUIRED', '100', 'Môn học cơ bản về lập trình'),
('TIN102', 'Data Structures', 'Cấu trúc dữ liệu và giải thuật', 1, 3, 2, 2, 'REQUIRED', '100', 'Môn học về cấu trúc dữ liệu'),
('TIN201', 'Object-Oriented Programming', 'Lập trình hướng đối tượng', 1, 3, 2, 2, 'REQUIRED', '200', 'Lập trình hướng đối tượng với Java'),
('TIN202', 'Database Systems', 'Hệ quản trị CSDL', 1, 2, 2, 2, 'REQUIRED', '200', 'Hệ quản trị cơ sở dữ liệu'),
('TIN301', 'Software Engineering', 'Kỹ thuật phần mềm', 1, 2, 2, 2, 'REQUIRED', '300', 'Kỹ thuật phát triển phần mềm'),
('TIN302', 'Web Development', 'Phát triển ứng dụng web', 1, 2, 1, 2, 'ELECTIVE', '300', 'Phát triển ứng dụng web hiện đại')
ON CONFLICT (code) DO NOTHING;

-- Create view for course statistics
CREATE OR REPLACE VIEW course_statistics AS
SELECT 
    c.id,
    c.code,
    c.vietnamese_name,
    c.credits,
    c.course_type,
    c.course_level,
    COUNT(co.id) as offering_count,
    COUNT(e.id) as enrollment_count,
    AVG(e.final_numeric_grade) as average_grade,
    COUNT(CASE WHEN e.is_passed = true THEN 1 END) as passed_count,
    COUNT(CASE WHEN e.is_passed = false THEN 1 END) as failed_count
FROM courses c
LEFT JOIN course_offerings co ON c.id = co.course_id AND co.is_active = true
LEFT JOIN enrollments e ON co.id = e.course_offering_id AND e.status = 'COMPLETED'
WHERE c.is_active = true
GROUP BY c.id, c.code, c.vietnamese_name, c.credits, c.course_type, c.course_level
ORDER BY c.code;
