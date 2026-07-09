package com.hrms.events;

import com.hrms.snapshot.EmployeeAuditSnapshot;

public record EmployeeUpdatedEvent(

        EmployeeAuditSnapshot oldSnapshot,

        EmployeeAuditSnapshot newSnapshot,

        Long performedByEmployeeId,

        String performedByUsername

) {
} 
