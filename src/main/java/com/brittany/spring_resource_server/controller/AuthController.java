package com.brittany.spring_resource_server.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.brittany.spring_resource_server.service.TokenService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(Authentication authentication) {
        String accessToken=tokenService.generateToken(authentication);

        return ResponseEntity.ok().body(Map.of("accessToken",accessToken));
    }

}
