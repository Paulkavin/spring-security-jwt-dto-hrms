package com.hrms.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name="employees")
@Getter
@Setter
@NoArgsConstructor //Frameworks like Hibernate/JPA require a no-argument constructor to instantiate database entities before filling them with data.
@AllArgsConstructor // t allows you to initialize all object properties at the moment of creation in a single line of code
@Builder // helpsto avoid multiple over loaded constructor , creates complex objects using method chaining.
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String department;

    private String role;

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}
