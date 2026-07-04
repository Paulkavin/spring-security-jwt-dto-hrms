package com.hrms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hrms.dto.LeaveRequestDTO;
import com.hrms.dto.LeaveResponseDTO;
import com.hrms.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    // Employee applies leave
    @PostMapping
    @PreAuthorize("hasAuthority('LEAVE_CREATE')")
    public ResponseEntity<LeaveResponseDTO> applyLeave(
            @RequestBody LeaveRequestDTO request,
            Authentication authentication) {

        LeaveResponseDTO response = leaveService.applyLeave(
                request,
                authentication.getName());

        return ResponseEntity.ok(response);
    }

    // Employee views own leaves
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('LEAVE_READ_OWN')")
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaves(
            Authentication authentication) {

        return ResponseEntity.ok(
                leaveService.getMyLeaves(authentication.getName()));
    }

     @GetMapping
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<List<LeaveResponseDTO>> getAllLeaves() {

        return ResponseEntity.ok(
                leaveService.getAllLeaves());
    }

    @PutMapping("/{leaveId}/approve")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<LeaveResponseDTO> approveLeave(
            @PathVariable Long leaveId) {

        return ResponseEntity.ok(
                leaveService.approveLeave(leaveId));
    }

    @PutMapping("/{leaveId}/reject")
    @PreAuthorize("hasAuthority('LEAVE_REJECT')")
    public ResponseEntity<LeaveResponseDTO> rejectLeave(
            @PathVariable Long leaveId) {

        return ResponseEntity.ok(
                leaveService.rejectLeave(leaveId));
    }

}

