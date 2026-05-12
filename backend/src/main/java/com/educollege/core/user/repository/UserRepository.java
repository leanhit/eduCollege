package com.educollege.core.user.repository;

import com.educollege.core.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username
    Optional<User> findByUsername(String username);

    // Find by email
    Optional<User> findByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find by role
    List<User> findByRole(String role);

    // Find by status
    List<User> findByStatus(String status);

    // Find by role and status
    List<User> findByRoleAndStatus(String role, String status);

    // Find by id key
    Optional<User> findByIdKey(String idKey);

    // Check if id key exists
    boolean existsByIdKey(String idKey);

    // Find users created after date
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);

    // Find users with last login after date
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :date")
    List<User> findUsersWithLastLoginAfter(@Param("date") LocalDateTime date);

    // Count users by role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") String role);

    // Count active users
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();
}
