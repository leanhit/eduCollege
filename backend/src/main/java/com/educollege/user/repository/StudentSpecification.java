package com.educollege.user.repository;

import com.educollege.user.model.Student;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

/**
 * Dynamic specifications for Student entity
 */
public class StudentSpecification {

    public static Specification<Student> hasFacultyId(Long facultyId) {
        return (root, query, cb) -> facultyId == null ? null : cb.equal(root.get("faculty").get("id"), facultyId);
    }

    public static Specification<Student> hasClassGroupId(Long classGroupId) {
        return (root, query, cb) -> classGroupId == null ? null : cb.equal(root.get("classGroup").get("id"), classGroupId);
    }

    public static Specification<Student> hasEnrollmentYear(Integer year) {
        return (root, query, cb) -> year == null ? null : cb.equal(root.get("enrollmentYear"), year);
    }

    public static Specification<Student> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Student> searchByNameOrCode(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("user").get("username")), pattern),
                cb.like(cb.lower(root.get("studentNumber")), pattern)
            );
        };
    }
}
