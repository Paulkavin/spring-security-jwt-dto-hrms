package com.hrms.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import com.hrms.audit.service.EmployeeAuditService;
import com.hrms.events.EmployeeUpdatedEvent;
@Component
@RequiredArgsConstructor
public class EmployeeAuditListener {

    private final EmployeeAuditService employeeAuditService;

    @EventListener
    public void handle(EmployeeUpdatedEvent event) {

        employeeAuditService.createAudit(event);

    }

}
