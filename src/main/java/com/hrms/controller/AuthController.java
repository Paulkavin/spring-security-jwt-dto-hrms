package com.hrms.controller;

import com.hrms.dto.LoginRequestDTO;
import com.hrms.dto.LoginResponseDTO;
import com.hrms.dto.RefreshTokenRequestDTO;
import com.hrms.dto.RefreshTokenResponseDTO;
import com.hrms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody LoginRequestDTO dto){

        return authService.login(dto);

    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO dto){
        return authService.refreshToken(dto);
    }

}