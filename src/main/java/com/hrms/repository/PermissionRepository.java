package com.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}