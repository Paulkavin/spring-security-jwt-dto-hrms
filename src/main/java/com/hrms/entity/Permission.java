package com.hrms.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="permissions")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //LAZY - loads when necessary
    @ManyToOne(fetch = FetchType.EAGER) // Module - many permissions to one module
    @JoinColumn(name="module_id",nullable=false)
    private Module module;

    @Column(nullable = false)
    private String action;

    private String description;
    private Boolean active = true;// if we want, we can make inactive without deleting it

    @ManyToMany(mappedBy = "permissions")
private Set<Role> roles = new HashSet<>();
}
