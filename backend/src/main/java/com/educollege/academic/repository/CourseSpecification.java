package com.educollege.academic.repository;

import com.educollege.academic.model.Course;
import org.springframework.data.jpa.domain.Specification;

/**
 * Dynamic specifications for Course entity
 */
public class CourseSpecification {

    public static Specification<Course> hasFacultyId(Long facultyId) {
        return (root, query, cb) -> facultyId == null ? null : cb.equal(root.get("faculty").get("id"), facultyId);
    }

    public static Specification<Course> hasCredits(Integer credits) {
        return (root, query, cb) -> credits == null ? null : cb.equal(root.get("credits"), credits);
    }

    public static Specification<Course> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("code")), pattern),
                cb.like(cb.lower(root.get("vietnameseName")), pattern)
            );
        };
    }

    public static Specification<Course> isActive(Boolean active) {
        return (root, query, cb) -> active == null ? null : cb.equal(root.get("isActive"), active);
    }
}
