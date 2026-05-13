-- Seed Data for eduCollege University System
-- This script inserts initial academic data for testing and development

-- Insert into eduCollege Database
-- Note: This should be run against educollege_db

-- Default academic roles (if academic_roles table exists)
INSERT INTO academic_roles (id, name, description, permissions) VALUES 
('role_admin', 'ADMIN', 'System Administrator', '{"all": true}'),
('role_faculty', 'FACULTY', 'Faculty Member', '{"academic": true, "teaching": true}'),
('role_student', 'STUDENT', 'Student', '{"learning": true, "enrollment": true}'),
('role_advisor', 'ADVISOR', 'Academic Advisor', '{"advising": true, "counseling": true}')
ON CONFLICT (id) DO NOTHING;

-- Sample users for testing
INSERT INTO users (id, email, password_hash, first_name, last_name, role, status, email_verified) VALUES 
('user_admin_001', 'admin@educollege.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System', 'Administrator', 'ADMIN', TRUE),
('user_demo_001', 'john.student@educollege.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Student', 'STUDENT', 'ACTIVE', TRUE),
('user_demo_002', 'jane.faculty@educollege.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jane', 'Faculty', 'FACULTY', 'ACTIVE', TRUE),
('user_demo_003', 'bob.advisor@educollege.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Bob', 'Advisor', 'ADVISOR', 'ACTIVE', FALSE)
ON CONFLICT (email) DO NOTHING;

-- User profiles for eduCollege
INSERT INTO user_profiles (user_id, first_name, last_name, phone, timezone, language, bio) VALUES 
('user_admin_001', 'System', 'Administrator', '+1-555-0100', 'UTC', 'en', 'System administrator account'),
('user_demo_001', 'John', 'Student', '+1-555-0101', 'America/New_York', 'en', 'Computer Science student passionate about learning'),
('user_demo_002', 'Jane', 'Faculty', '+1-555-0102', 'America/Los_Angeles', 'en', 'Computer Science professor with 10 years of teaching experience'),
('user_demo_003', 'Bob', 'Advisor', '+1-555-0103', 'Europe/London', 'en', 'Academic advisor specializing in student counseling')
ON CONFLICT (user_id) DO NOTHING;

-- User preferences
INSERT INTO user_preferences (user_id, email_notifications, sms_notifications, push_notifications, theme, currency, privacy_profile_visibility) VALUES 
('user_admin_001', TRUE, FALSE, TRUE, 'dark', 'USD', 'PUBLIC'),
('user_demo_001', TRUE, TRUE, TRUE, 'light', 'USD', 'PUBLIC'),
('user_demo_002', FALSE, FALSE, TRUE, 'auto', 'USD', 'PRIVATE'),
('user_demo_003', TRUE, FALSE, FALSE, 'light', 'EUR', 'PUBLIC')
ON CONFLICT (user_id) DO NOTHING;

-- User addresses
INSERT INTO user_addresses (user_id, address_type, street_address, city, state_province, postal_code, country, is_default) VALUES 
('user_demo_001', 'HOME', '123 Main St', 'New York', 'NY', '10001', 'USA', TRUE),
('user_demo_001', 'WORK', '456 Business Ave', 'New York', 'NY', '10002', 'USA', FALSE),
('user_demo_002', 'HOME', '789 Oak Blvd', 'Los Angeles', 'CA', '90001', 'USA', TRUE),
('user_demo_003', 'HOME', '321 Pine St', 'London', NULL, 'SW1A 0AA', 'UK', TRUE)
ON CONFLICT DO NOTHING;

-- Academic Departments for eduCollege
INSERT INTO academic_departments (id, name, code, description, dean_id, status) VALUES 
('dept_cs_001', 'Computer Science', 'CS', 'Department of Computer Science and Engineering', 'user_demo_002', 'ACTIVE'),
('dept_math_001', 'Mathematics', 'MATH', 'Department of Mathematics and Statistics', NULL, 'ACTIVE'),
('dept_eng_001', 'Engineering', 'ENG', 'Department of Engineering', NULL, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- Academic Programs
INSERT INTO academic_programs (id, department_id, name, code, degree_type, duration_years, status) VALUES 
('prog_cs_bachelor', 'dept_cs_001', 'Bachelor of Computer Science', 'CS-BS', 'BACHELOR', 4, 'ACTIVE'),
('prog_cs_master', 'dept_cs_001', 'Master of Computer Science', 'CS-MS', 'MASTER', 2, 'ACTIVE'),
('prog_math_bachelor', 'dept_math_001', 'Bachelor of Mathematics', 'MATH-BS', 'BACHELOR', 4, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- Academic Advisors
INSERT INTO academic_advisors (id, user_id, department_id, title, specialization, office_location, phone, email, max_students, current_students, status) VALUES 
('advisor_001', 'user_demo_003', 'dept_cs_001', 'Academic Advisor', 'Student Counseling', 'Room 301', '+1-555-0103', 'bob.advisor@educollege.edu', 50, 25, 'ACTIVE'),
('advisor_002', 'user_demo_002', 'dept_cs_001', 'Faculty Advisor', 'Computer Science', 'Room 205', '+1-555-0102', 'jane.faculty@educollege.edu', 30, 15, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- Sample Courses
INSERT INTO academic_courses (id, department_id, course_code, title, description, credits, prerequisites, status) VALUES 
('course_cs101', 'dept_cs_001', 'CS101', 'Introduction to Computer Science', 'Fundamental concepts of programming and computer science', 3, '[]', 'ACTIVE'),
('course_cs201', 'dept_cs_001', 'CS201', 'Data Structures', 'Advanced data structures and algorithms', 4, '["CS101"]', 'ACTIVE'),
('course_cs301', 'dept_cs_001', 'CS301', 'Database Systems', 'Database design and management', 3, '["CS101", "CS201"]', 'ACTIVE'),
('course_math101', 'dept_math_001', 'MATH101', 'Calculus I', 'Introduction to differential and integral calculus', 4, '[]', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- Course Offerings
INSERT INTO academic_course_offerings (id, course_id, semester, year, instructor_id, max_students, current_students, schedule, status) VALUES 
('offering_cs101_fall24', 'course_cs101', 'FALL', 2024, 'user_demo_002', 30, 25, '{"days": ["MON", "WED", "FRI"], "time": "10:00-11:30", "room": "CS-101"}', 'ACTIVE'),
('offering_cs201_fall24', 'course_cs201', 'FALL', 2024, 'user_demo_002', 25, 20, '{"days": ["TUE", "THU"], "time": "14:00-15:30", "room": "CS-201"}', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- Sample Enrollments
INSERT INTO academic_enrollments (id, student_id, course_offering_id, enrollment_date, status, grade) VALUES 
('enroll_001', 'user_demo_001', 'offering_cs101_fall24', '2024-08-15', 'ENROLLED', NULL),
('enroll_002', 'user_demo_001', 'offering_cs201_fall24', '2024-08-15', 'ENROLLED', NULL)
ON CONFLICT (student_id, course_offering_id) DO NOTHING;

-- Advising Sessions
INSERT INTO academic_advising_sessions (id, student_id, advisor_id, session_date, session_type, notes, status, follow_up_required) VALUES 
('session_001', 'user_demo_001', 'advisor_001', '2024-09-01 14:00:00', 'ACADEMIC_PLANNING', 'Discussion about course selection for Spring 2025', 'COMPLETED', TRUE),
('session_002', 'user_demo_001', 'advisor_001', '2024-09-15 15:30:00', 'CAREER_COUNSELING', 'Career goals and internship opportunities', 'SCHEDULED', FALSE)
ON CONFLICT (id) DO NOTHING;

RAISE NOTICE 'eduCollege seed data loaded successfully!';
