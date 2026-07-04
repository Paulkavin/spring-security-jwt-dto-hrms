package com.hrms.dto;

import java.time.LocalDate;

import com.hrms.enums.LeaveType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveRequestDTO {

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

}