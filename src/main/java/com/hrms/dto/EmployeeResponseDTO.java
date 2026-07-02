package com.hrms.dto;

import lombok.*;
import java.time.LocalDate;

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

    // private String role;

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}
