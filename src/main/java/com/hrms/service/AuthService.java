package com.hrms.service;

import com.hrms.dto.LoginRequestDTO;
import com.hrms.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO dto);

}