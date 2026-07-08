package com.hrms.service;

import com.hrms.dto.LoginRequestDTO;
import com.hrms.dto.LoginResponseDTO;
import com.hrms.dto.RefreshTokenRequestDTO;
import com.hrms.dto.RefreshTokenResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO dto);

    RefreshTokenResponseDTO refreshToken(
        RefreshTokenRequestDTO dto
);
}