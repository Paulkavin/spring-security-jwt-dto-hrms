package com.hrms.audit.entity;

import com.hrms.audit.enums.AuditAction;
import com.hrms.audit.model.FieldChange;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name="employee_audit_log")
public class EmployeeAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON) //This field should be stored as JSON.
    @Column(columnDefinition="json") //Create this column using the JSON datatype.
    private List<FieldChange> changes;

    private Long performedByEmployeeId;
    private String performedByUsername;

    private LocalDateTime performedAt;
}
