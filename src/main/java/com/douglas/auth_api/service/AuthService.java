package com.douglas.auth_api.service;

import com.douglas.auth_api.dto.AuthResponse;
import com.douglas.auth_api.dto.LoginRequest;
import com.douglas.auth_api.dto.RegisterRequest;
import com.douglas.auth_api.entity.User;
import com.douglas.auth_api.entity.Role;
import com.douglas.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        //MONTA O OBJETO USER COM A SENHA CRIPTOGRAFADA
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode (request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return AuthResponse.builder()
                .accessToken(jwt)
                .build();

    }

    public AuthResponse login(LoginRequest request) {
        //AUTENTICA O USUARIO - LANCA EXCECAO SE EMAIL/SENHA ESTIVER ERRADO
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .accessToken(token)
                .build();
    }
}
