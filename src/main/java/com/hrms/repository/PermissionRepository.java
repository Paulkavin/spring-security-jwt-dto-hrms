package com.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.hrms.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    public Optional<Permission>  findByAuthority(String authority);
}