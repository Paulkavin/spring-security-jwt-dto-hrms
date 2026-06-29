package com.hrms.dto;

import lombok.*;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;

  
    private String firstName;

    private String lastName;

    private String email;


    private String department;

    private String role;

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}
