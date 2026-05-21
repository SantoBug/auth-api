package com.douglas.auth_api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginRequest {
    private String email;
    private String password;
}
