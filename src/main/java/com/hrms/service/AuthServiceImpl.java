package com.hrms.service;

import com.hrms.dto.LoginRequestDTO;
import com.hrms.dto.LoginResponseDTO;
import com.hrms.dto.RefreshTokenRequestDTO;
import com.hrms.dto.RefreshTokenResponseDTO;
import com.hrms.security.JwtUtil;
import com.hrms.security.CustomUserDetailsService;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        
        private final AuthenticationManager authenticationManager;
        @Autowired
        private JwtUtil jwtUtil;

        private final CustomUserDetailsService customUserDetailsService;
        

        @Override
        public LoginResponseDTO login(LoginRequestDTO dto) {
                // Spring security login process
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                dto.getEmail(),
                                                dto.getPassword()));

                String accessToken = jwtUtil.generateAccessToken(dto.getEmail());

                String refreshToken = jwtUtil.generateRefreshToken(dto.getEmail());

                return new LoginResponseDTO(
                                accessToken,
                                refreshToken,
                                "Bearer");
        }

        @Override
        public RefreshTokenResponseDTO refreshToken(
                        RefreshTokenRequestDTO dto) {

                String refreshToken = dto.getRefreshToken();

                // 1
                if (!jwtUtil.isRefreshToken(refreshToken)) {
                        throw new IllegalArgumentException(
                                        "Invalid refresh token.");
                }

                // 2
                String username = jwtUtil.extractUsername(refreshToken);

                // 3
                UserDetails userDetails = customUserDetailsService
                                .loadUserByUsername(username);

                // 4
                if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                        throw new IllegalArgumentException(
                                        "Refresh token expired or invalid.");
                }

                // 5
                String newAccessToken = jwtUtil.generateAccessToken(username);

                return new RefreshTokenResponseDTO(
                                newAccessToken,
                                "Bearer");

        }

}