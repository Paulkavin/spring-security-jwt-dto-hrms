package com.hrms.security.session.entity;

import com.hrms.entity.Employee;
import com.hrms.security.session.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_session",
        indexes = {
                @Index(name = "idx_session_id", columnList = "session_id"),
                @Index(name = "idx_employee_status", columnList = "employee_id,status"),
                @Index(name = "idx_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Public session identifier.
     * Stored inside JWT.
     * Never expose database ID.
     */
    @Column(name = "session_id", nullable = false, unique = true, length = 50)
    private String sessionId;

    /**
     * Employee owning this session.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //optional =false, the associated entity or field cannot be null
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * ACTIVE / REVOKED / LOGGED_OUT / EXPIRED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    /**
     * Login timestamp.
     */
    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    /**
     * Updated whenever user accesses APIs.
     * (Can later be optimized to update periodically instead of every request.)
     */
    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;

    /**
     * Enterprise session expiry.
     * Independent of JWT expiry.
     */
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    /**
     * When session was revoked.
     */
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    /**
     * Future:
     * Admin/HR/System who revoked the session.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revoked_by_employee_id")
    private Employee revokedByEmployee;

    /**
     * Browser / Mobile / OS
     */
    @Column(name = "device_info", length = 500)
    private String deviceInfo;

    /**
     * Client IP
     */
    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Automatically populate timestamps.
     */
    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }

        loginTime = now;
        lastAccessTime = now;

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}

/**
 * @PrePersist: Triggers before a new entity is inserted into the database for the first time.
 * @PreUpdate: Triggers before an existing entity is modified/updated in the database
 */