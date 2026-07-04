package com.hrms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.enums.LeaveStatus;
import com.hrms.enums.LeaveType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveResponseDTO {

    private Long id;
    // Can't return employee object here, coz of sensitive information
    private String employeeName;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private LeaveStatus status;

    private LocalDateTime appliedAt;

}