package com.hrms.service;

import java.util.List;

import com.hrms.dto.EmployeeRequestDTO;
import com.hrms.dto.EmployeeResponseDTO;

public interface EmployeeService {
    //public abstract
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    List <EmployeeResponseDTO> getAllEmployees();
    
    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO updateEmployeeId(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);
    EmployeeResponseDTO getProfile(String email);

    //for adding roles
    EmployeeResponseDTO assignRoles(Long employeeId, List<String> roleNames);
    
    // For Assign Extra Permissions
   void assignPermissionToEmployee(
        Long employeeId,
        Long permissionId);

    // Remove Permissions

    void removePermissionFromEmployee(
        Long employeeId,
        Long permissionId);



}

/*
    Interface, By default
     Automatically abstract (except for static or default methods).Example: void save(); is exactly the same as public abstract void save();
     Access Modfier - Public

     Why Interface Here ?
     The controller depends on the abstraction, not the implementation. Later, if you have another implementation (for example, a mock service in tests), the controller doesn't need to change.
*/