-- EduCollege Database Migration Script
-- Version 2.0
-- Creates indexes for performance optimization

-- User indexes
CREATE INDEX IF NOT EXISTS idx_users_vietnamese_id ON users(vietnamese_id);
CREATE INDEX IF NOT EXISTS idx_users_faculty_id ON users(faculty_id);
CREATE INDEX IF NOT EXISTS idx_users_class_id ON users(class_id);
CREATE INDEX IF NOT EXISTS idx_users_department_id ON users(department_id);
CREATE INDEX IF NOT EXISTS idx_users_id_category ON users(id_category);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);

-- Academic structure indexes
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

-- Course indexes
CREATE INDEX IF NOT EXISTS idx_courses_code ON courses(code);
CREATE INDEX IF NOT EXISTS idx_courses_department_id ON courses(department_id);
CREATE INDEX IF NOT EXISTS idx_courses_course_type ON courses(course_type);
CREATE INDEX IF NOT EXISTS idx_courses_is_active ON courses(is_active);

CREATE INDEX IF NOT EXISTS idx_semesters_code ON semesters(code);
CREATE INDEX IF NOT EXISTS idx_semesters_academic_year ON semesters(academic_year);
CREATE INDEX IF NOT EXISTS idx_semesters_is_active ON semesters(is_active);

CREATE INDEX IF NOT EXISTS idx_course_offerings_code ON course_offerings(code);
CREATE INDEX IF NOT EXISTS idx_course_offerings_course_id ON course_offerings(course_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_semester_id ON course_offerings(semester_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_teacher_id ON course_offerings(teacher_id);
CREATE INDEX IF NOT EXISTS idx_course_offerings_status ON course_offerings(status);
CREATE INDEX IF NOT EXISTS idx_course_offerings_is_active ON course_offerings(is_active);

-- Student indexes
CREATE INDEX IF NOT EXISTS idx_students_user_id ON students(user_id);
CREATE INDEX IF NOT EXISTS idx_students_student_number ON students(student_number);
CREATE INDEX IF NOT EXISTS idx_students_faculty_id ON students(faculty_id);
CREATE INDEX IF NOT EXISTS idx_students_class_id ON students(class_id);
CREATE INDEX IF NOT EXISTS idx_students_academic_standing ON students(academic_standing);
CREATE INDEX IF NOT EXISTS idx_students_student_status ON students(student_status);
CREATE INDEX IF NOT EXISTS idx_students_is_active ON students(is_active);

-- Teacher indexes
CREATE INDEX IF NOT EXISTS idx_teachers_user_id ON teachers(user_id);
CREATE INDEX IF NOT EXISTS idx_teachers_teacher_number ON teachers(teacher_number);
CREATE INDEX IF NOT EXISTS idx_teachers_department_id ON teachers(department_id);
CREATE INDEX IF NOT EXISTS idx_teachers_is_active ON teachers(is_active);
CREATE INDEX IF NOT EXISTS idx_teachers_is_advisor ON teachers(is_advisor);

-- Enrollment indexes
CREATE INDEX IF NOT EXISTS idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course_offering_id ON enrollments(course_offering_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_status ON enrollments(status);
CREATE INDEX IF NOT EXISTS idx_enrollments_enrollment_date ON enrollments(enrollment_date);
CREATE INDEX IF NOT EXISTS idx_enrollments_semester_id ON enrollments(course_offering_id) WHERE course_offering_id IS NOT NULL;

-- Sequence indexes
CREATE INDEX IF NOT EXISTS idx_sequences_sequence_key ON sequences(sequence_key);
CREATE INDEX IF NOT EXISTS idx_sequences_sequence_type ON sequences(sequence_type);
CREATE INDEX IF NOT EXISTS idx_sequences_faculty_id ON sequences(faculty_id);
CREATE INDEX IF NOT EXISTS idx_sequences_department_id ON sequences(department_id);
CREATE INDEX IF NOT EXISTS idx_sequences_class_group_id ON sequences(class_group_id);
CREATE INDEX IF NOT EXISTS idx_sequences_year ON sequences(year);
CREATE INDEX IF NOT EXISTS idx_sequences_is_active ON sequences(is_active);

-- Advising indexes
CREATE INDEX IF NOT EXISTS idx_advising_sessions_student_id ON advising_sessions(student_id);
CREATE INDEX IF NOT EXISTS idx_advising_sessions_teacher_id ON advising_sessions(teacher_id);
CREATE INDEX IF NOT EXISTS idx_advising_sessions_semester_id ON advising_sessions(semester_id);
CREATE INDEX IF NOT EXISTS idx_advising_sessions_session_date ON advising_sessions(session_date);
CREATE INDEX IF NOT EXISTS idx_advising_sessions_status ON advising_sessions(status);

-- Finance indexes
CREATE INDEX IF NOT EXISTS idx_tuition_fees_student_id ON tuition_fees(student_id);
CREATE INDEX IF NOT EXISTS idx_tuition_fees_semester_id ON tuition_fees(semester_id);
CREATE INDEX IF NOT EXISTS idx_tuition_fees_status ON tuition_fees(status);
CREATE INDEX IF NOT EXISTS idx_tuition_fees_due_date ON tuition_fees(due_date);

CREATE INDEX IF NOT EXISTS idx_payment_transactions_tuition_fee_id ON payment_transactions(tuition_fee_id);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_transaction_date ON payment_transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_status ON payment_transactions(status);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_reference_number ON payment_transactions(reference_number);
