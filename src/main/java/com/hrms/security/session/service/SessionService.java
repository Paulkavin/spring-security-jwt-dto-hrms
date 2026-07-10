package com.hrms.security.session.service;

import com.hrms.entity.Employee;
import com.hrms.security.session.entity.UserSession;

import java.util.Optional;

public interface SessionService {

    /**
     * Creates a new ACTIVE session.
     *
     * @param employee Logged-in employee
     * @param deviceInfo Browser / Device information
     * @param ipAddress Client IP address
     * @return Newly created session
     */
    UserSession createSession(
            Employee employee,
            String deviceInfo,
            String ipAddress
    );

    /**
     * Checks whether employee already has an ACTIVE session.
     *
     * @param employee Employee
     * @return true if active session exists
     */
    boolean hasActiveSession(Employee employee);

    /**
     * Returns ACTIVE session if available.
     *
     * @param employee Employee
     * @return Active session
     */
    Optional<UserSession> getActiveSession(Employee employee);

    /**
     * Marks session as REVOKED.
     *
     * Used during Force Login.
     *
     * @param session Existing session
     */
    void revokeSession(UserSession session);

    /**
     * Marks session as LOGGED_OUT.
     *
     * Used during Logout.
     *
     * @param sessionId Session UUID
     */
    void logout(String sessionId);

    /**
     * Validates session.
     *
     * Used inside JwtAuthenticationFilter.
     *
     * Checks:
     * - Session exists
     * - ACTIVE
     * - Not expired
     *
     * @param sessionId Session UUID
     * @return true if valid
     */
    boolean validateSession(String sessionId);

    /**
     * Updates last access timestamp.
     *
     * @param sessionId Session UUID
     */
    void updateLastAccess(String sessionId);

}