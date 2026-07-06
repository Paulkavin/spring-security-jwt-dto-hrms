package com.hrms.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}
