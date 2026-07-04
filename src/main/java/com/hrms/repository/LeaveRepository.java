package com.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Employee;
import com.hrms.entity.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    // TP show my leaves of employee
    List<Leave> findByEmployee(Employee employee);

}