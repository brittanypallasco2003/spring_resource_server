package com.brittany.spring_resource_server.controller.DTOs;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, LocalDateTime timestamp) {

}