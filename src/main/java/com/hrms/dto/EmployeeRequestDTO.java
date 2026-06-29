package com.hrms.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotBlank
    private String firstName;

    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    
    private String department;

    private String role;

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}
