package com.brittany.spring_resource_server.controller.DTOs;

import com.brittany.spring_resource_server.models.GrantTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(

        @NotNull(message = "El grantType es obligatorio") 
        GrantTypeEnum grantType, String username, String password,
        boolean withRefreshToken, String refreshToken) {

}
