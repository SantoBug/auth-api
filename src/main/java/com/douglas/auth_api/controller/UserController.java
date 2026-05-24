package com.douglas.auth_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> me(
            @AuthenticationPrincipal UserDetails userDetails ){

        return  ResponseEntity.ok(
                "Usuário autenticado: " + userDetails.getUsername()
        );
    }

}

