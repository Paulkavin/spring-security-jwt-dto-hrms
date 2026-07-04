package com.hrms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.LeaveRequestDTO;
import com.hrms.dto.LeaveResponseDTO;
import com.hrms.entity.Employee;
import com.hrms.entity.Leave;
import com.hrms.enums.LeaveStatus;
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
                        new RuntimeException("Employee not found"));

        // Basic Validation
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException(
                    "End date cannot be before start date");
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
                        new RuntimeException("Employee not found"));

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
                    new RuntimeException("Leave not found"));

    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new RuntimeException(
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
                    new RuntimeException("Leave not found"));

    if (leave.getStatus() != LeaveStatus.PENDING) {
        throw new RuntimeException(
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

}