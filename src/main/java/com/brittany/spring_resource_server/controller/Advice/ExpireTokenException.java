package com.brittany.spring_resource_server.controller.Advice;

public class ExpireTokenException extends RuntimeException {
    public ExpireTokenException(String message) {
        super(message);
    }
}
