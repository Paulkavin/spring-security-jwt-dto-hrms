package com.hrms.security.session.service;

import com.hrms.entity.Employee;
import com.hrms.security.session.entity.UserSession;
import com.hrms.security.session.enums.SessionStatus;
import com.hrms.security.session.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    /**
     * Session validity period.
     */
    @Value("${session.expiry-days}")
    private long sessionExpiryDays;

    @Override
    public UserSession createSession(
            Employee employee,
            String deviceInfo,
            String ipAddress
    ) {

        LocalDateTime now = LocalDateTime.now();

        UserSession session = UserSession.builder()
                .employee(employee)
                .status(SessionStatus.ACTIVE)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .expiryTime(now.plusDays(sessionExpiryDays))
                .build();

        return sessionRepository.save(session);
    }

    @Override
    public boolean hasActiveSession(Employee employee) {

        return sessionRepository.existsByEmployeeAndStatus(
                employee,
                SessionStatus.ACTIVE
        );

    }

    @Override
    public Optional<UserSession> getActiveSession(Employee employee) {

        return sessionRepository.findByEmployeeAndStatus(
                employee,
                SessionStatus.ACTIVE
        );

    }

    @Override
    public void revokeSession(UserSession session) {

        session.setStatus(SessionStatus.REVOKED);
        session.setRevokedAt(LocalDateTime.now());

        sessionRepository.save(session);

    }

    @Override
    public void logout(String sessionId) {

        UserSession session = sessionRepository
                .findBySessionId(sessionId)
                .orElseThrow(() ->
                        new RuntimeException("Session not found.")
                );

        session.setStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

    }

    @Override
    public boolean validateSession(String sessionId) {

        Optional<UserSession> optionalSession =
                sessionRepository.findBySessionId(sessionId);

        if (optionalSession.isEmpty()) {
            return false;
        }

        UserSession session = optionalSession.get();

        if (session.getStatus() != SessionStatus.ACTIVE) {
            return false;
        }

        if (session.getExpiryTime().isBefore(LocalDateTime.now())) {

            session.setStatus(SessionStatus.EXPIRED);
            sessionRepository.save(session);

            return false;
        }

        return true;

    }

    @Override
    public void updateLastAccess(String sessionId) {

        sessionRepository.findBySessionId(sessionId)
                .ifPresent(session -> {

                    session.setLastAccessTime(LocalDateTime.now());

                    sessionRepository.save(session);

                });

    }

}