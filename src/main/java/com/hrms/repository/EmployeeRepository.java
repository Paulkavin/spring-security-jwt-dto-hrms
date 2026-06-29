package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Employee;

public interface EmployeeRepository extends
JpaRepository<Employee,Long> {
    
    Optional<Employee> findByEmail(String email);
    /*
    Looks up an employee using their email address. It wraps the result in an Optional so you can safely handle the case where the email does not exist in the database.
    */
    boolean existsByEmail(String email);

    /*
    As long as you follow Spring's naming conventions (like starting with findBy, existsBy, or countBy followed by the exact variable name), Spring will handle the database logic for you.
    */
}
