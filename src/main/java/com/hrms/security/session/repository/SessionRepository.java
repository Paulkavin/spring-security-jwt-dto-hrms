package com.hrms.security.session.repository;

import com.hrms.entity.Employee;
import com.hrms.security.session.entity.UserSession;
import com.hrms.security.session.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<UserSession, Long> {

    /**
     * Find session by UUID stored inside JWT.
     */
    Optional<UserSession> findBySessionId(String sessionId);

    /**
     * Find currently active session.
     */
    Optional<UserSession> findByEmployeeAndStatus(
            Employee employee,
            SessionStatus status
    );

    /**
     * Check whether active session exists.
     */
    boolean existsByEmployeeAndStatus(
            Employee employee,
            SessionStatus status
    );

    /**
     * Find sessions by status.
     */
    List<UserSession> findByStatus(SessionStatus status);

    /**
     * Cleanup job.
     */
    List<UserSession> findByStatusAndExpiryTimeBefore(
            SessionStatus status,
            LocalDateTime time
    );

}