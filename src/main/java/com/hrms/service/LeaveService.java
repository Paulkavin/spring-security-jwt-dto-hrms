package com.hrms.service;

import java.util.List;

import com.hrms.dto.LeaveRequestDTO;
import com.hrms.dto.LeaveResponseDTO;

public interface LeaveService {

    LeaveResponseDTO applyLeave(
            LeaveRequestDTO request,
            String employeeEmail);

    List<LeaveResponseDTO> getMyLeaves(
            String employeeEmail);
    
    LeaveResponseDTO approveLeave(Long leaveId);

    LeaveResponseDTO rejectLeave(Long leaveId);

    List<LeaveResponseDTO> getAllLeaves();

    LeaveResponseDTO updateLeave(
        Long leaveId,
        LeaveRequestDTO request,
        String employeeEmail);

void deleteLeave(
        Long leaveId,
        String employeeEmail);
}