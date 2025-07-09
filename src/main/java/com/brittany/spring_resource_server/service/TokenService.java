package com.brittany.spring_resource_server.service;

import com.brittany.spring_resource_server.controller.DTOs.TokenRequest;

public interface TokenService {
    public String generateToken(TokenRequest request);

     
}
