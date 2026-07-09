package com.hrms.audit.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.hrms.audit.entity.EmployeeAuditLog;
import com.hrms.audit.enums.AuditAction;
import com.hrms.audit.model.FieldChange;
import com.hrms.audit.repository.EmployeeAuditRepository;
import com.hrms.events.EmployeeUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeAuditService {

    private final EmployeeAuditRepository repository;

    public void createAudit(EmployeeUpdatedEvent event) {

        List<FieldChange> changes = new ArrayList<>();

        compare(
                "firstName",
                event.oldSnapshot().getFirstName(),
                event.newSnapshot().getFirstName(),
                changes
        );

        compare(
                "lastName",
                event.oldSnapshot().getLastName(),
                event.newSnapshot().getLastName(),
                changes
        );

        compare(
                "email",
                event.oldSnapshot().getEmail(),
                event.newSnapshot().getEmail(),
                changes
        );

        compare(
                "department",
                event.oldSnapshot().getDepartment(),
                event.newSnapshot().getDepartment(),
                changes
        );

        compare(
                "phone",
                event.oldSnapshot().getPhone(),
                event.newSnapshot().getPhone(),
                changes
        );

        compare(
                "salary",
                String.valueOf(event.oldSnapshot().getSalary()),
                String.valueOf(event.newSnapshot().getSalary()),
                changes
        );

        compare(
                "joiningDate",
                String.valueOf(event.oldSnapshot().getJoiningDate()),
                String.valueOf(event.newSnapshot().getJoiningDate()),
                changes
        );

        compare(
                "status",
                event.oldSnapshot().getStatus(),
                event.newSnapshot().getStatus(),
                changes
        );

        if (changes.isEmpty()) {
            return;
        }

        EmployeeAuditLog audit = new EmployeeAuditLog();

        audit.setEmployeeId(event.newSnapshot().getId());

        audit.setAction(AuditAction.UPDATE);

        audit.setEventType("EMPLOYEE_UPDATED");

        audit.setChanges(changes);

        audit.setPerformedByEmployeeId(event.performedByEmployeeId());

        audit.setPerformedByUsername(event.performedByUsername());

        audit.setPerformedAt(LocalDateTime.now());

        repository.save(audit);
    }

    private void compare(
            String field,
            String oldValue,
            String newValue,
            List<FieldChange> changes
    ) {

        if (!Objects.equals(oldValue, newValue)) {

            changes.add(
                    new FieldChange(
                            field,
                            oldValue,
                            newValue
                    )
            );

        }

    }

}