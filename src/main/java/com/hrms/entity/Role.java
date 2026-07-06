package com.hrms.entity;

import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    private String displayName;

    private String description;

    private Boolean active=true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="role_permissions",
            joinColumns=@JoinColumn(name="role_id"),
            inverseJoinColumns=@JoinColumn(name="permission_id")
    )
    private Set<Permission> permissions=new HashSet<>();

    @ManyToMany(mappedBy = "roles")
private Set<Employee> employees = new HashSet<>();


}

/**
 * By writing @ManyToMany(mappedBy = "roles") inside your Role entity, you are telling Hibernate:
 * The Owner: The Employee entity is the "owner" of this relationship.The Target Field: Hibernate must look at the field named roles inside the Employee class to find the actual join table configuration (like @JoinTable, table name, and foreign keys).
 * The Database Impact: Do not create a separate join table for the Role class. Use the same join table that Employee is using.
 */
