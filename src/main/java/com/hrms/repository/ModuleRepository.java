package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByName(String name);

}