package com.educollege.core.user.profile.repository;

import com.educollege.core.user.profile.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Profile Repository - Data access layer for UserProfile entity
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Find user profile by user ID
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Find user profile by user ID with user details fetched
     */
    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user WHERE up.user.id = :userId")
    Optional<UserProfile> findByUserIdWithUser(@Param("userId") Long userId);

    /**
     * Check if profile exists for user
     */
    boolean existsByUserId(Long userId);

    /**
     * Find profiles by department
     */
    List<UserProfile> findByDepartment(String department);

    /**
     * Find profiles by major
     */
    List<UserProfile> findByMajor(String major);

    /**
     * Find profiles by year of study
     */
    List<UserProfile> findByYearOfStudy(String yearOfStudy);

    /**
     * Find faculty profiles by department
     */
    @Query("SELECT up FROM UserProfile up WHERE up.user.role = 'FACULTY' AND up.department = :department")
    List<UserProfile> findFacultyByDepartment(@Param("department") String department);

    /**
     * Find student profiles by year of study
     */
    @Query("SELECT up FROM UserProfile up WHERE up.user.role = 'STUDENT' AND up.yearOfStudy = :yearOfStudy")
    List<UserProfile> findStudentsByYearOfStudy(@Param("yearOfStudy") String yearOfStudy);

    /**
     * Find profiles with GPA above threshold
     */
    @Query("SELECT up FROM UserProfile up WHERE up.gpa >= :minGpa ORDER BY up.gpa DESC")
    List<UserProfile> findStudentsWithGpaAbove(@Param("minGpa") Double minGpa);

    /**
     * Search profiles by name (first or last name)
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
           "LOWER(up.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(up.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<UserProfile> searchByName(@Param("name") String name);

    /**
     * Find public profiles
     */
    List<UserProfile> findByIsProfilePublicTrue();

    /**
     * Find profiles by profile status
     */
    List<UserProfile> findByProfileStatus(String profileStatus);

    /**
     * Count profiles by department
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.department = :department")
    long countByDepartment(@Param("department") String department);

    /**
     * Count profiles by major
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.major = :major")
    long countByMajor(@Param("major") String major);
}
