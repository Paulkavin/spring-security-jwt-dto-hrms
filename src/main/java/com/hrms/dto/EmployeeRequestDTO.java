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

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}

/**
 * When you place @Data over a class, Lombok automatically injects the following annotations behind the scenes at compile time:
 * @ToString: Generates a standard toString() method printing all class fields.
 * @EqualsAndHashCode: Generates equals() and hashCode() implementations based on all non-static fields.
 * @Getter: Generates getter methods for all fields.
 * @Setter: Generates setter methods for all non-final fields.
 * @RequiredArgsConstructor: Generates a constructor for all final fields and fields marked @NonNull.
 */