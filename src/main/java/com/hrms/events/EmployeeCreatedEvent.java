package com.hrms.events;

import java.time.LocalDate;

public record EmployeeCreatedEvent(

        Long employeeId,

        String firstName,

        String lastName,

        String email,

        String department,

        String phone,

        Double salary,

        LocalDate joiningDate,

        String status

) {
}