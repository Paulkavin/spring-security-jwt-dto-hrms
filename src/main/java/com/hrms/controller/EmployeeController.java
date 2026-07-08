package com.hrms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.hrms.dto.AssignPermissionRequestDTO;
import com.hrms.dto.EmployeeRequestDTO;
import com.hrms.dto.EmployeeResponseDTO;
import com.hrms.security.CustomUserDetails;
import com.hrms.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public EmployeeResponseDTO getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        return employeeService.updateEmployeeId(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee Deleted Successfully!";
    }

    @GetMapping("/me")
    // Authentication Principal - makes the employee see themself alone
    public EmployeeResponseDTO myProfile(@AuthenticationPrincipal CustomUserDetails user) {
        return employeeService.getProfile(user.getUsername());
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponseDTO assignRoles(
            @PathVariable Long id,
            @RequestBody List<String> roleNames) {

        return employeeService.assignRoles(id, roleNames);
    }

    @PostMapping("/{employeeId}/permissions")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<String> assignPermissionToEmployee(
            @PathVariable Long employeeId,
            @RequestBody AssignPermissionRequestDTO request) {

        employeeService.assignPermissionToEmployee(
                employeeId,
                request.getPermissionId());

        return ResponseEntity.ok("Permission assigned successfully.");
    }

    // Delete Permissions
    @DeleteMapping("/{employeeId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<String> removePermissionFromEmployee(
            @PathVariable Long employeeId,
            @PathVariable Long permissionId) {

        employeeService.removePermissionFromEmployee(
                employeeId,
                permissionId);

        return ResponseEntity.ok("Permission removed successfully.");
    }

}

/***
 * Summary of the FlowClient sends a POST request with JSON to /employees.Spring
 * maps the JSON to EmployeeRequestDTO.@Valid checks the DTO for rules
 * (like @NotBlank). If it fails, a 400 Bad Request is sent back instantly.If
 * validation passes, the controller calls the createEmployee method on the
 * EmployeeService interface.Spring redirects this call to the concrete logic
 * written inside EmployeeServiceImpl.The service saves the data using the
 * repository, maps it to an EmployeeResponseDTO, and returns it back up the
 * chain to the client.
 * Spring Dependency Injection (DI) - Because your controller class is marked
 * with @RestController, Spring manages its lifecycle. When Spring Boot starts
 * up:It scans your codebase and detects that EmployeeServiceImpl is annotated
 * with @Service. Spring instantiates it and stores it in its context as a
 * reusable bean.It looks at your EmployeeController constructor and sees it
 * requires an implementation of the EmployeeService interface.Spring looks into
 * its context, finds the EmployeeServiceImpl bean, and automatically injects it
 * into your controller's employeeService variable.
 */