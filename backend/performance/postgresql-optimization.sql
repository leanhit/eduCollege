-- PostgreSQL Performance Optimization for EduCollege University System

-- 1. Memory Configuration
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET work_mem = '4MB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';

-- 2. Connection Configuration
ALTER SYSTEM SET max_connections = '200';

-- 3. Reload Configuration
SELECT pg_reload_conf();

-- 4. Create Performance Indexes for EduCollege
-- Users table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_email ON users(email);

-- Academic module indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_courses_code ON courses(code);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_course_offerings_course_id ON course_offerings(course_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_enrollments_student_id ON enrollments(student_id);

-- Finance module indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tuition_fees_student_id ON tuition_fees(student_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_payment_transactions_fee_id ON payment_transactions(tuition_fee_id);

COMMIT;
