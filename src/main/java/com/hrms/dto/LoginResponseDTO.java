package com.hrms.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private boolean success;
    private String code;
    private String message;
    private boolean forceLoginRequired;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private EmployeeResponseDTO employee;
}
