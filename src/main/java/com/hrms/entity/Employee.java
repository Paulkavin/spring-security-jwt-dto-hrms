package com.hrms.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="employees")
@Getter
@Setter
@NoArgsConstructor //Frameworks like Hibernate/JPA require a no-argument constructor to instantiate database entities before filling them with data.
@AllArgsConstructor // it allows you to initialize all object properties at the moment of creation in a single line of code
@Builder // helps to avoid multiple over loaded constructor , creates complex objects using method chaining.
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

    
    
    // private String role;
    @ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
        name = "employee_roles",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
)

@Builder.Default // without using it new HashSet will be ignored

private Set<Role> roles = new HashSet<>();

//Direct Permissions
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
        name = "employee_permissions",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
)
private Set<Permission> directPermissions = new HashSet<>();

    private String phone;
    
    private Double salary;

    private LocalDate joiningDate;

    private String status;
}
