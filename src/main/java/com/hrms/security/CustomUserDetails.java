package com.hrms.security;

import com.hrms.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * CustomUserDetails is an adapter. It converts your application's user model into the format that Spring Security understands.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Employee employee;
     private final Collection<? extends GrantedAuthority> authorities;
    /* Old
    @Override
    //returns roles/permissions
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For RBAC , spring reads like ROLE +----
        return List.of(new SimpleGrantedAuthority("ROLE_"+employee.getRole()));
    }
 */

    @Override
public Collection<? extends GrantedAuthority> getAuthorities() {

    return authorities;
}

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(employee.getStatus());
    }
}

/* New System
Employee

↓

Roles

↓

Permissions

↓

Authorities
    
*/