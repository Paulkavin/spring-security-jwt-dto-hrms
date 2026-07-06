package com.hrms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.LeaveRequestDTO;
import com.hrms.dto.LeaveResponseDTO;
import com.hrms.entity.Employee;
import com.hrms.entity.Leave;
import com.hrms.enums.LeaveStatus;
import com.hrms.exception.BusinessValidationException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public LeaveResponseDTO applyLeave(
            LeaveRequestDTO request,
            String employeeEmail) {

        Employee employee = employeeRepository
                .findByEmail(employeeEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        // All Validations
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessValidationException(
                    "End date cannot be before start date");
        }
        if (request.getLeaveType() == null) {
    throw new BusinessValidationException(
            "Leave type is required");
}
        if (request.getReason() == null ||
    request.getReason().trim().isEmpty()) {

    throw new BusinessValidationException(
            "Reason is required");
}
if (request.getStartDate() == null) {
    throw new BusinessValidationException(
            "Start date is required");
}
if (request.getEndDate() == null) {
    throw new BusinessValidationException(
            "End date is required");
}
if (request.getStartDate().isBefore(LocalDate.now())) {

    throw new BusinessValidationException(
            "Cannot apply leave for past dates");
}

long days = ChronoUnit.DAYS.between(
        request.getStartDate(),
        request.getEndDate());

if (days > 30) {

    throw new BusinessValidationException(
            "Leave cannot exceed 30 days");
}



        Leave leave = new Leave();

        leave.setEmployee(employee);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());

        leave.setStatus(LeaveStatus.PENDING);
        leave.setAppliedAt(LocalDateTime.now());

        Leave savedLeave = leaveRepository.save(leave);

        return mapToResponse(savedLeave);
    }

    @Override
    public List<LeaveResponseDTO> getMyLeaves(
            String employeeEmail) {

        Employee employee = employeeRepository
                .findByEmail(employeeEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return leaveRepository.findByEmployee(employee)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //Approve Leave
    @Override
public LeaveResponseDTO approveLeave(Long leaveId) {

    Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Leave not found"));

    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new BusinessValidationException(
                "Only pending leave can be approved");
    }

    leave.setStatus(LeaveStatus.APPROVED);

    Leave updatedLeave = leaveRepository.save(leave);

    return mapToResponse(updatedLeave);
}

//Reject Leave
@Override
public LeaveResponseDTO rejectLeave(Long leaveId) {

    Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Leave not found"));

    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new BusinessValidationException(
                "Only pending leave can be rejected");
    }

    leave.setStatus(LeaveStatus.REJECTED);

    Leave updatedLeave = leaveRepository.save(leave);

    return mapToResponse(updatedLeave);
}

//View All Leaves
@Override
public List<LeaveResponseDTO> getAllLeaves() {

    return leaveRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
}

    private LeaveResponseDTO mapToResponse(Leave leave) {

    return LeaveResponseDTO.builder()
            .id(leave.getId())
            .employeeName(
                    leave.getEmployee().getFirstName()
                    + " "
                    + leave.getEmployee().getLastName())
            .leaveType(leave.getLeaveType())
            .startDate(leave.getStartDate())
            .endDate(leave.getEndDate())
            .reason(leave.getReason())
            .status(leave.getStatus())
            .appliedAt(leave.getAppliedAt())
            .build();
}

@Override
public LeaveResponseDTO updateLeave(
        Long leaveId,
        LeaveRequestDTO request,
        String employeeEmail) {

    Employee employee = employeeRepository.findByEmail(employeeEmail)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Employee not found"));

    Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Leave not found"));

    // Only owner can update
    if (!leave.getEmployee().getId().equals(employee.getId())) {
        throw new BusinessValidationException(
                "You can update only your own leave");
    }

    // Only pending leave
    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new BusinessValidationException(
                "Only pending leave can be updated");
    }

    // Date validation
    if (request.getEndDate().isBefore(request.getStartDate())) {
        throw new BusinessValidationException(
                "End date cannot be before start date");
    }

    leave.setLeaveType(request.getLeaveType());
    leave.setStartDate(request.getStartDate());
    leave.setEndDate(request.getEndDate());
    leave.setReason(request.getReason());

    Leave updated = leaveRepository.save(leave);

    return mapToResponse(updated);
}

        @Override
public void deleteLeave(
        Long leaveId,
        String employeeEmail) {

    Employee employee = employeeRepository.findByEmail(employeeEmail)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Employee not found"));

    Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Leave not found"));

    // Only owner
    if (!leave.getEmployee().getId().equals(employee.getId())) {
        throw new BusinessValidationException(
                "You can delete only your own leave");
    }

    // Only pending
    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new BusinessValidationException(
                "Only pending leave can be deleted");
    }

    leaveRepository.delete(leave);
}

}
/**
 * What is ChronoUnit?ChronoUnit is a standard enum in Java's java.time.temporal package.Purpose: 
 * It represents standard date and time units (like days, hours, weeks, or months).
 * Function: The .between() method calculates the amount of time between two temporal objects of the same type.
 * Result: It always returns a long value.
 */