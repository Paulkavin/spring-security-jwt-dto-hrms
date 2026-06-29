package com.hrms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hrms.dto.EmployeeRequestDTO;
import com.hrms.dto.EmployeeResponseDTO;
import com.hrms.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService employeeService;
     
    @PostMapping
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeRequestDTO dto){
         return employeeService.createEmployee(dto);
    }

    @GetMapping
    public List<EmployeeResponseDTO> getAllEmployees(){
return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployee(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto ){
        return employeeService.updateEmployeeId(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return "Employee Deleted Successfully!";
    }


}

/***
 * Summary of the FlowClient sends a POST request with JSON to /employees.Spring maps the JSON to EmployeeRequestDTO.@Valid checks the DTO for rules (like @NotBlank). If it fails, a 400 Bad Request is sent back instantly.If validation passes, the controller calls the createEmployee method on the EmployeeService interface.Spring redirects this call to the concrete logic written inside EmployeeServiceImpl.The service saves the data using the repository, maps it to an EmployeeResponseDTO, and returns it back up the chain to the client.
 * Spring Dependency Injection (DI) - Because your controller class is marked with @RestController, Spring manages its lifecycle. When Spring Boot starts up:It scans your codebase and detects that EmployeeServiceImpl is annotated with @Service. Spring instantiates it and stores it in its context as a reusable bean.It looks at your EmployeeController constructor and sees it requires an implementation of the EmployeeService interface.Spring looks into its context, finds the EmployeeServiceImpl bean, and automatically injects it into your controller's employeeService variable.
 */