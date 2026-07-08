package com.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String tokenType;
}
