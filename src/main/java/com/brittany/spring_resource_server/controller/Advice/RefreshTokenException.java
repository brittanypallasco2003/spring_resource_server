package com.brittany.spring_resource_server.controller.Advice;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
