-- EduCollege Database Migration Script
-- Version 4.0
-- Creates materialized views for performance optimization

-- Materialized view for student academic summary
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_student_academic_summary AS
SELECT 
    s.id AS student_id,
    s.student_number AS vietnamese_id,
    u.username,
    u.email,
    f.code AS faculty_code,
    f.vietnamese_name AS faculty_name,
    cg.code AS class_code,
    s.enrollment_year,
    s.graduation_year,
    s.current_gpa,
    s.cumulative_gpa,
    s.total_credits,
    s.completed_credits,
    s.failed_credits,
    s.academic_standing,
    s.student_status,
    COUNT(DISTINCT e.id) AS total_enrollments,
    COUNT(DISTINCT CASE WHEN e.status = 'COMPLETED' THEN e.id END) AS completed_courses,
    COUNT(DISTINCT CASE WHEN e.status = 'FAILED' THEN e.id END) AS failed_courses,
    AVG(e.grade) AS average_grade,
    AVG(e.attendance_rate) AS average_attendance,
    MAX(e.updated_at) AS last_activity
FROM students s
LEFT JOIN users u ON s.user_id = u.id
LEFT JOIN faculties f ON s.faculty_id = f.id
LEFT JOIN class_groups cg ON s.class_id = cg.id
LEFT JOIN enrollments e ON s.id = e.student_id
WHERE s.is_active = true
GROUP BY s.id, s.student_number, u.username, u.email, f.code, f.vietnamese_name, 
         cg.code, s.enrollment_year, s.graduation_year, s.current_gpa, s.cumulative_gpa,
         s.total_credits, s.completed_credits, s.failed_credits, s.academic_standing, s.student_status
WITH DATA;

-- Create index on materialized view
CREATE INDEX IF NOT EXISTS idx_mv_student_academic_summary_vietnamese_id 
    ON mv_student_academic_summary(vietnamese_id);
CREATE INDEX IF NOT EXISTS idx_mv_student_academic_summary_faculty_code 
    ON mv_student_academic_summary(faculty_code);
CREATE INDEX IF NOT EXISTS idx_mv_student_academic_summary_class_code 
    ON mv_student_academic_summary(class_code);
CREATE INDEX IF NOT EXISTS idx_mv_student_academic_summary_academic_standing 
    ON mv_student_academic_summary(academic_standing);
CREATE INDEX IF NOT EXISTS idx_mv_student_academic_summary_student_status 
    ON mv_student_academic_summary(student_status);

-- Materialized view for faculty statistics
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_faculty_statistics AS
SELECT 
    f.id AS faculty_id,
    f.code AS faculty_code,
    f.vietnamese_name AS faculty_name,
    COUNT(DISTINCT s.id) AS total_students,
    COUNT(DISTINCT CASE WHEN s.student_status = 'ENROLLED' THEN s.id END) AS enrolled_students,
    COUNT(DISTINCT CASE WHEN s.student_status = 'GRADUATED' THEN s.id END) AS graduated_students,
    COUNT(DISTINCT CASE WHEN s.student_status = 'DROPPED' THEN s.id END) AS dropped_students,
    AVG(s.current_gpa) AS average_gpa,
    AVG(s.cumulative_gpa) AS average_cumulative_gpa,
    SUM(s.total_credits) AS total_credits,
    SUM(s.completed_credits) AS total_completed_credits,
    SUM(s.failed_credits) AS total_failed_credits,
    COUNT(DISTINCT CASE WHEN s.academic_standing = 'GOOD' THEN s.id END) AS good_standing,
    COUNT(DISTINCT CASE WHEN s.academic_standing = 'PROBATION' THEN s.id END) AS probation,
    COUNT(DISTINCT CASE WHEN s.academic_standing = 'SUSPENDED' THEN s.id END) AS suspended
FROM faculties f
LEFT JOIN students s ON f.id = s.faculty_id AND s.is_active = true
WHERE f.is_active = true
GROUP BY f.id, f.code, f.vietnamese_name
WITH DATA;

-- Create index on faculty statistics materialized view
CREATE INDEX IF NOT EXISTS idx_mv_faculty_statistics_code 
    ON mv_faculty_statistics(faculty_code);

-- Materialized view for course statistics
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_course_statistics AS
SELECT 
    c.id AS course_id,
    c.code AS course_code,
    c.vietnamese_name AS course_name,
    c.credits,
    c.course_type,
    d.code AS department_code,
    d.vietnamese_name AS department_name,
    f.code AS faculty_code,
    COUNT(DISTINCT co.id) AS total_offerings,
    COUNT(DISTINCT e.id) AS total_enrollments,
    COUNT(DISTINCT CASE WHEN e.status = 'COMPLETED' THEN e.id END) AS completed_enrollments,
    COUNT(DISTINCT CASE WHEN e.status = 'FAILED' THEN e.id END) AS failed_enrollments,
    AVG(e.grade) AS average_grade,
    AVG(e.gpa_points) AS average_gpa_points,
    AVG(e.attendance_rate) AS average_attendance,
    MAX(e.updated_at) AS last_activity
FROM courses c
LEFT JOIN departments d ON c.department_id = d.id
LEFT JOIN faculties f ON d.faculty_id = f.id
LEFT JOIN course_offerings co ON c.id = co.course_id AND co.is_active = true
LEFT JOIN enrollments e ON co.id = e.course_offering_id
WHERE c.is_active = true
GROUP BY c.id, c.code, c.vietnamese_name, c.credits, c.course_type, d.code, d.vietnamese_name, f.code
WITH DATA;

-- Create index on course statistics materialized view
CREATE INDEX IF NOT EXISTS idx_mv_course_statistics_code 
    ON mv_course_statistics(course_code);
CREATE INDEX IF NOT EXISTS idx_mv_course_statistics_type 
    ON mv_course_statistics(course_type);
CREATE INDEX IF NOT EXISTS idx_mv_course_statistics_department 
    ON mv_course_statistics(department_code);

-- Materialized view for enrollment statistics by semester
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_semester_enrollment_statistics AS
SELECT 
    sem.id AS semester_id,
    sem.code AS semester_code,
    sem.vietnamese_name AS semester_name,
    sem.academic_year,
    sem.semester_number,
    COUNT(DISTINCT e.id) AS total_enrollments,
    COUNT(DISTINCT e.student_id) AS unique_students,
    COUNT(DISTINCT co.id) AS total_course_offerings,
    COUNT(DISTINCT CASE WHEN e.status = 'COMPLETED' THEN e.id END) AS completed_enrollments,
    COUNT(DISTINCT CASE WHEN e.status = 'FAILED' THEN e.id END) AS failed_enrollments,
    AVG(e.grade) AS average_grade,
    AVG(e.gpa_points) AS average_gpa_points,
    AVG(e.attendance_rate) AS average_attendance
FROM semesters sem
LEFT JOIN course_offerings co ON sem.id = co.semester_id AND co.is_active = true
LEFT JOIN enrollments e ON co.id = e.course_offering_id
WHERE sem.is_active = true
GROUP BY sem.id, sem.code, sem.vietnamese_name, sem.academic_year, sem.semester_number
WITH DATA;

-- Create index on semester enrollment statistics materialized view
CREATE INDEX IF NOT EXISTS idx_mv_semester_enrollment_statistics_code 
    ON mv_semester_enrollment_statistics(semester_code);
CREATE INDEX IF NOT EXISTS idx_mv_semester_enrollment_statistics_year 
    ON mv_semester_enrollment_statistics(academic_year);

-- Function to refresh all materialized views
CREATE OR REPLACE FUNCTION refresh_all_materialized_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_student_academic_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_faculty_statistics;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_statistics;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_semester_enrollment_statistics;
END;
$$ language 'plpgsql';

-- Create trigger to refresh materialized views after important data changes
CREATE OR REPLACE FUNCTION trigger_refresh_student_summary()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_student_academic_summary;
    RETURN NULL;
END;
$$ language 'plpgsql';

-- Note: In production, you might want to schedule refreshes instead of using triggers
-- to avoid performance impact during high-traffic periods
