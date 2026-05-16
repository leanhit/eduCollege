-- EduCollege Database Migration Script
-- Version 3.0
-- Creates constraints and triggers for data integrity

-- Update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update timestamp triggers to relevant tables
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_faculties_updated_at ON faculties;
CREATE TRIGGER update_faculties_updated_at BEFORE UPDATE ON faculties FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_departments_updated_at ON departments;
CREATE TRIGGER update_departments_updated_at BEFORE UPDATE ON departments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_majors_updated_at ON majors;
CREATE TRIGGER update_majors_updated_at BEFORE UPDATE ON majors FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_class_groups_updated_at ON class_groups;
CREATE TRIGGER update_class_groups_updated_at BEFORE UPDATE ON class_groups FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_courses_updated_at ON courses;
CREATE TRIGGER update_courses_updated_at BEFORE UPDATE ON courses FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_semesters_updated_at ON semesters;
CREATE TRIGGER update_semesters_updated_at BEFORE UPDATE ON semesters FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_course_offerings_updated_at ON course_offerings;
CREATE TRIGGER update_course_offerings_updated_at BEFORE UPDATE ON course_offerings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_students_updated_at ON students;
CREATE TRIGGER update_students_updated_at BEFORE UPDATE ON students FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_teachers_updated_at ON teachers;
CREATE TRIGGER update_teachers_updated_at BEFORE UPDATE ON teachers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_enrollments_updated_at ON enrollments;
CREATE TRIGGER update_enrollments_updated_at BEFORE UPDATE ON enrollments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_sequences_updated_at ON sequences;
CREATE TRIGGER update_sequences_updated_at BEFORE UPDATE ON sequences FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_advising_sessions_updated_at ON advising_sessions;
CREATE TRIGGER update_advising_sessions_updated_at BEFORE UPDATE ON advising_sessions FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_tuition_fees_updated_at ON tuition_fees;
CREATE TRIGGER update_tuition_fees_updated_at BEFORE UPDATE ON tuition_fees FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_payment_transactions_updated_at ON payment_transactions;
CREATE TRIGGER update_payment_transactions_updated_at BEFORE UPDATE ON payment_transactions FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Check constraints
ALTER TABLE users DROP CONSTRAINT IF EXISTS chk_vietnamese_id_format;
ALTER TABLE users ADD CONSTRAINT chk_vietnamese_id_format 
    CHECK (vietnamese_id ~ '^(SV|GV|NV)[A-Z0-9]+$');

ALTER TABLE courses DROP CONSTRAINT IF EXISTS chk_credits_positive;
ALTER TABLE courses ADD CONSTRAINT chk_credits_positive 
    CHECK (credits > 0);

ALTER TABLE course_offerings DROP CONSTRAINT IF EXISTS chk_max_students_positive;
ALTER TABLE course_offerings ADD CONSTRAINT chk_max_students_positive 
    CHECK (max_students > 0);

ALTER TABLE course_offerings DROP CONSTRAINT IF EXISTS chk_current_students_not_negative;
ALTER TABLE course_offerings ADD CONSTRAINT chk_current_students_not_negative 
    CHECK (current_students >= 0);

ALTER TABLE enrollments DROP CONSTRAINT IF EXISTS chk_grade_range;
ALTER TABLE enrollments ADD CONSTRAINT chk_grade_range 
    CHECK (grade IS NULL OR (grade >= 0 AND grade <= 10));

ALTER TABLE enrollments DROP CONSTRAINT IF EXISTS chk_attendance_rate;
ALTER TABLE enrollments ADD CONSTRAINT chk_attendance_rate 
    CHECK (attendance_rate IS NULL OR (attendance_rate >= 0 AND attendance_rate <= 100));

ALTER TABLE enrollments DROP CONSTRAINT IF EXISTS chk_gpa_points_range;
ALTER TABLE enrollments ADD CONSTRAINT chk_gpa_points_range 
    CHECK (gpa_points IS NULL OR (gpa_points >= 0 AND gpa_points <= 4.0));

ALTER TABLE students DROP CONSTRAINT IF EXISTS chk_student_gpa_range;
ALTER TABLE students ADD CONSTRAINT chk_student_gpa_range 
    CHECK (current_gpa >= 0 AND current_gpa <= 4.0);

ALTER TABLE students DROP CONSTRAINT IF EXISTS chk_cumulative_gpa_range;
ALTER TABLE students ADD CONSTRAINT chk_cumulative_gpa_range 
    CHECK (cumulative_gpa >= 0 AND cumulative_gpa <= 4.0);

ALTER TABLE students DROP CONSTRAINT IF EXISTS chk_credits_not_negative;
ALTER TABLE students ADD CONSTRAINT chk_credits_not_negative 
    CHECK (total_credits >= 0 AND completed_credits >= 0 AND failed_credits >= 0);

ALTER TABLE tuition_fees DROP CONSTRAINT IF EXISTS chk_tuition_amount_positive;
ALTER TABLE tuition_fees ADD CONSTRAINT chk_tuition_amount_positive 
    CHECK (total_amount >= 0 AND paid_amount >= 0);

ALTER TABLE payment_transactions DROP CONSTRAINT IF EXISTS chk_payment_amount_positive;
ALTER TABLE payment_transactions ADD CONSTRAINT chk_payment_amount_positive 
    CHECK (amount >= 0);

-- Trigger to update class group student count
CREATE OR REPLACE FUNCTION update_class_student_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE class_groups 
        SET current_students = current_students + 1 
        WHERE id = NEW.class_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE class_groups 
        SET current_students = current_students - 1 
        WHERE id = OLD.class_id;
    END IF;
    RETURN COALESCE(NEW, OLD);
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS trg_update_class_student_count ON students;
CREATE TRIGGER trg_update_class_student_count
    AFTER INSERT OR DELETE ON students
    FOR EACH ROW EXECUTE FUNCTION update_class_student_count();

-- Trigger to update course offering student count
CREATE OR REPLACE FUNCTION update_course_offering_student_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE course_offerings 
        SET current_students = current_students + 1 
        WHERE id = NEW.course_offering_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE course_offerings 
        SET current_students = current_students - 1 
        WHERE id = OLD.course_offering_id;
    END IF;
    RETURN COALESCE(NEW, OLD);
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS trg_update_course_offering_student_count ON enrollments;
CREATE TRIGGER trg_update_course_offering_student_count
    AFTER INSERT OR DELETE ON enrollments
    FOR EACH ROW EXECUTE FUNCTION update_course_offering_student_count();

-- Trigger to update tuition fee paid amount
CREATE OR REPLACE FUNCTION update_tuition_fee_paid_amount()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' AND NEW.status = 'SUCCESS' THEN
        UPDATE tuition_fees 
        SET paid_amount = paid_amount + NEW.amount,
            status = CASE 
                WHEN paid_amount + NEW.amount >= total_amount THEN 'PAID'
                ELSE 'PARTIAL'
            END
        WHERE id = NEW.tuition_fee_id;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS trg_update_tuition_fee_paid_amount ON payment_transactions;
CREATE TRIGGER trg_update_tuition_fee_paid_amount
    AFTER INSERT ON payment_transactions
    FOR EACH ROW EXECUTE FUNCTION update_tuition_fee_paid_amount();

-- Function to check course offering capacity
CREATE OR REPLACE FUNCTION check_course_capacity()
RETURNS TRIGGER AS $$
DECLARE
    current_count INTEGER;
    max_count INTEGER;
BEGIN
    IF TG_OP = 'INSERT' THEN
        SELECT current_students, max_students 
        INTO current_count, max_count
        FROM course_offerings 
        WHERE id = NEW.course_offering_id;
        
        IF current_count >= max_count THEN
            RAISE EXCEPTION 'Course offering is full (current: %, max: %)', current_count, max_count;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS trg_check_course_capacity ON enrollments;
CREATE TRIGGER trg_check_course_capacity
    BEFORE INSERT ON enrollments
    FOR EACH ROW EXECUTE FUNCTION check_course_capacity();
