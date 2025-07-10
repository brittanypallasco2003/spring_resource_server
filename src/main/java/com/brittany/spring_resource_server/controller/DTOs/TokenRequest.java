package com.brittany.spring_resource_server.controller.DTOs;

public record TokenRequest(String grantType, String username, String password, boolean withRefreshToken, String refreshToken) {

}
