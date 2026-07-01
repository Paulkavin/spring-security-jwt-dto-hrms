package com.hrms.service;

import com.hrms.dto.LoginRequestDTO;
import com.hrms.dto.LoginResponseDTO;
import com.hrms.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
        
       
        
        @Autowired
    private final AuthenticationManager authenticationManager;
 @Autowired
        private JwtUtil jwtUtil;
    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        //Spring security login process 
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        String token = jwtUtil.generateToken(dto.getEmail());

return new LoginResponseDTO(token);
    }
}