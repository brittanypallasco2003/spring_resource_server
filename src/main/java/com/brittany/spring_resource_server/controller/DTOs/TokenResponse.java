package com.brittany.spring_resource_server.controller.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"accessToken", "refreshToken","tokenType", "expiresIn"})
public record TokenResponse(String accessToken, String refreshToken, String tokenType, Long expiresIn) {

}
