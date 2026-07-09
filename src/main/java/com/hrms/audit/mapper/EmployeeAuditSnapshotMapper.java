package com.hrms.audit.mapper;

import org.springframework.stereotype.Component;

import com.hrms.entity.Employee;
import com.hrms.snapshot.EmployeeAuditSnapshot;

@Component
public class EmployeeAuditSnapshotMapper {

    public EmployeeAuditSnapshot toSnapshot(Employee employee) {

        return EmployeeAuditSnapshot.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .status(employee.getStatus())
                .build();
    }
}