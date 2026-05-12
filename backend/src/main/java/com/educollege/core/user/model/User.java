package com.educollege.core.user.model;

import com.educollege.core.shared.base.AuditableEntity;
import com.educollege.core.user.profile.model.UserProfile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditableEntity {

    // ===== Basic Information =====
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    // ===== Role Information =====
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE";

    // ===== Security Information =====
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "account_non_locked", nullable = false)
    @Builder.Default
    private Boolean accountNonLocked = true;

    @Column(name = "account_non_locked_reason", length = 500)
    private String accountNonLockedReason;

    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;

    // ===== ID Key Information =====
    @Column(name = "id_key", unique = true, length = 50)
    private String idKey;

    @Column(name = "id_type", length = 50)
    private String idType;

    // ===== Profile Information =====
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    // Version for optimistic locking
    @Version
    private Long version;
}
