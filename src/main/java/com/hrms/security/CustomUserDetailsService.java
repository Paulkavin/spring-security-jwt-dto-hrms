package com.hrms.security;

import com.hrms.entity.Employee;
import com.hrms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// This class loads the user for spring.To check username(mail)
/**
 * CustomUserDetailsService loads the user from the database and converts the
 * Employee into a UserDetails object that Spring Security understands.
 */
public class CustomUserDetailsService
                implements UserDetailsService {

        private final EmployeeRepository employeeRepository;

        

        @Override
        // username = mail by spring
        public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {

                Employee employee = employeeRepository
                                .findByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                // return new CustomUserDetails(employee) with duplicates (if set used);
                // List<GrantedAuthority> authorities = new ArrayList<>();
                Set<GrantedAuthority> authorities = new HashSet<>();

                // Role Authorites
                employee.getRoles().forEach(role -> {

                        authorities.add(
                                        new SimpleGrantedAuthority("ROLE_" + role.getName()));

                        role.getPermissions().forEach(permission -> {

                                /*
                                 * String authority =
                                 * permission.getModule().getName()
                                 * + "_"
                                 * + permission.getAction();
                                 */
                                String authority = permission.getAuthority();
                                authorities.add(
                                                new SimpleGrantedAuthority(authority));

                        });

                });

                // Direct Permissions
                employee.getDirectPermissions().forEach(permission -> {

                        authorities.add(
                                        new SimpleGrantedAuthority(permission.getAuthority()));

                });

                authorities.forEach(a -> System.out.println(a.getAuthority()));
                return new CustomUserDetails(employee, authorities);

        }

}

/**
 * Latest Flow
 * User Login
 * ↓
 * Load Role Permissions
 * +
 * Load Employee Direct Permissions
 * ↓
 * Merge
 * ↓
 * JWT
 * ↓
 * @PreAuthorize()
 */