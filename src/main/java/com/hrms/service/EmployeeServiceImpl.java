package com.hrms.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hrms.audit.mapper.EmployeeAuditSnapshotMapper;
import com.hrms.dto.EmployeeRequestDTO;
import com.hrms.dto.EmployeeResponseDTO;
import com.hrms.entity.Employee;
import com.hrms.entity.Permission;

import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.HashSet;
import com.hrms.entity.Role;
import com.hrms.events.EmployeeCreatedEvent;
import com.hrms.events.EmployeeUpdatedEvent;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.RoleRepository;
import com.hrms.security.CustomUserDetails;
import com.hrms.snapshot.EmployeeAuditSnapshot;

@Service
@RequiredArgsConstructor // Alternate to @Autowired
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EmployeeAuditSnapshotMapper snapshotMapper;

    private Employee mapToEntity(EmployeeRequestDTO dto) {
        return Employee.builder().firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // Encoding password
                .department(dto.getDepartment())
                .phone(dto.getPhone())
                .salary(dto.getSalary())
                .joiningDate(dto.getJoiningDate())
                .status(dto.getStatus())
                .build();
    }

    private EmployeeResponseDTO mapToResponse(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .status(employee.getStatus())
                .build();
    }

    // CREATE
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Employee employee = mapToEntity(dto);

        // Get default role
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new ResourceNotFoundException("Default EMPLOYEE role not found"));

        // Assign role
        employee.getRoles().add(employeeRole);

        // Save
        Employee savedEmployee = employeeRepository.save(employee);

        eventPublisher.publishEvent(
                new EmployeeCreatedEvent(
                        savedEmployee.getId(),
                        savedEmployee.getFirstName(),
                        savedEmployee.getLastName(),
                        savedEmployee.getEmail(),
                        savedEmployee.getDepartment(),
                        savedEmployee.getPhone(),
                        savedEmployee.getSalary(),
                        savedEmployee.getJoiningDate(),
                        savedEmployee.getStatus()));

        return mapToResponse(savedEmployee);

    }

    // READ
    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // READ By ID
    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));// can use .get() as well
        return mapToResponse(employee);
    }

    // UPDATE
    @Override
    public EmployeeResponseDTO updateEmployeeId(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));
        // Creating Old Snapshot
        EmployeeAuditSnapshot oldSnapshot = snapshotMapper.toSnapshot(employee);

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDepartment(dto.getDepartment());
        employee.setPhone(dto.getPhone());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus());

        Employee updatedEmployee = employeeRepository.save(employee);

        // Creating new snapshot
        EmployeeAuditSnapshot newSnapshot = snapshotMapper.toSnapshot(updatedEmployee);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        Long performedByEmployeeId = currentUser.getEmployee().getId();
        String performedByUsername = currentUser.getUsername();
        // Publishing Event
        eventPublisher.publishEvent(

                new EmployeeUpdatedEvent(

                        oldSnapshot,

                        newSnapshot,

                        performedByEmployeeId,

                        performedByUsername

                )

        );

        return mapToResponse(updatedEmployee);

    }

    // DELETE
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));

        employeeRepository.delete(employee);

    }

    // PROFILE
    @Override
    public EmployeeResponseDTO getProfile(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));
        return mapToResponse(employee);
    }

    // Add roles
    @Override
    public EmployeeResponseDTO assignRoles(Long employeeId, List<String> roleNames) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));

        Set<Role> roles = new HashSet<>();

        for (String roleName : roleNames) {

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found : " + roleName));

            roles.add(role);
        }

        employee.setRoles(roles);

        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }

    // ASSIGN PERMISSIONS
    @Override
    public void assignPermissionToEmployee(Long employeeId, Long permissionId) {

        // Find Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));

        // Find Permission
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        // Check duplicate assignment
        if (employee.getDirectPermissions().contains(permission)) {
            throw new DuplicateResourceException("Permission already assigned to employee");
        }

        // Assign Permission
        employee.getDirectPermissions().add(permission);

        // Save
        employeeRepository.save(employee);
    }

    @Override
    public void removePermissionFromEmployee(
            Long employeeId,
            Long permissionId) {

        // Find Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not Found"));

        // Find Permission
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        // Check whether permission exists
        if (!employee.getDirectPermissions().contains(permission)) {
            throw new RuntimeException("Permission is not assigned to this employee");
        }

        // Remove permission
        employee.getDirectPermissions().remove(permission);

        // Save
        employeeRepository.save(employee);
    }
}

/*
 * The .builder() method activates the Builder Design Pattern generated by
 * Lombok's @Builder annotation.It acts as a factory method that creates a
 * temporary helper object (called a "builder").
 * You use this helper object to configure your main object step-by-step using
 * method chaining before finally creating it.
 * Employee emp = Employee.builder() // 1. Creates the builder helper object
 * .firstName("John") // 2. Sets the firstName field
 * .lastName("Doe") // 3. Sets the lastName field
 * .build(); // 4. Instantiates the final Employee object
 * 
 */

/*
 * 1. @OverrideThis annotation tells the compiler that this method is overriding
 * a method declared in a parent interface (likely an EmployeeService
 * interface). It prevents typos in the method name.2. public
 * List<EmployeeResponseDTO> getAllEmployees() {public: Accessible from any
 * other class (like your Controller).List<EmployeeResponseDTO>: The return
 * type. Instead of exposing raw database entities to the frontend, it returns a
 * filtered, secure list of DTO objects.3. return
 * employeeRepository.findAll()Calls your Spring Data JPA repository. This goes
 * to the database, executes a SELECT * FROM employee; query under the hood, and
 * returns a standard List<Employee>.4. .stream()Converts that standard
 * List<Employee> into a Stream of Employees. Think of a Stream as a conveyor
 * belt where employee objects flow one by one, allowing you to perform
 * operations on them sequentially.5. .map(this::mapToResponse)This is the
 * transformation step..map(...): Takes each item on the conveyor belt and
 * changes it into something else.this::mapToResponse: This is a Method
 * Reference. It tells the stream to pass each Employee object into a helper
 * method named mapToResponse that exists inside this same class (this). That
 * helper method converts an Employee entity into an EmployeeResponseDTO.6.
 * .toList();Closes the stream pipeline. It collects all the newly transformed
 * EmployeeResponseDTO objects off the conveyor belt and packages them back into
 * a regular, immutable List<EmployeeResponseDTO>.
 */