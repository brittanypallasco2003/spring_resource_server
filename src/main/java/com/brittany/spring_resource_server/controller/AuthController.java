package com.brittany.spring_resource_server.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.brittany.spring_resource_server.controller.DTOs.TokenRequest;
import com.brittany.spring_resource_server.controller.DTOs.TokenResponse;
import com.brittany.spring_resource_server.service.TokenService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenResponse response=tokenService.generateToken(request);

        return ResponseEntity.ok().body(response);
    }

}
