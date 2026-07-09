package com.hrms.snapshot;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmployeeAuditSnapshot {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String department;

    private String phone;

    private Double salary;

    private LocalDate joiningDate;

    private String status;

}
