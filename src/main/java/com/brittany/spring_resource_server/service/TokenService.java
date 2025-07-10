package com.brittany.spring_resource_server.service;

import com.brittany.spring_resource_server.controller.DTOs.TokenRequest;
import com.brittany.spring_resource_server.controller.DTOs.TokenResponse;

public interface TokenService {
    public TokenResponse generateToken(TokenRequest request);

     
}
