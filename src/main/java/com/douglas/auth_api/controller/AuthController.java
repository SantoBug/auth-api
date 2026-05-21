package com.douglas.auth_api.controller;

import com.douglas.auth_api.dto.RegisterRequest;
import com.douglas.auth_api.dto.LoginRequest;
import com.douglas.auth_api.dto.AuthResponse;
import com.douglas.auth_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 — recurso criado
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity
                .ok(authService.login(request)); // 200 — sucesso
    }
}