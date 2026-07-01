package com.hrms.security;

import com.hrms.entity.Employee;
import com.hrms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//This class loads the user for spring.To check username(mail)
/**
 * CustomUserDetailsService loads the user from the database and converts the Employee into a UserDetails object that Spring Security understands.
 */
public class CustomUserDetailsService
        implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    //username = mail by spring
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Employee employee = employeeRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(employee);

    }

}