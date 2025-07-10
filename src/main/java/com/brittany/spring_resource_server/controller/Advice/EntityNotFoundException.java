package com.brittany.spring_resource_server.controller.Advice;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message){
        super(message);
    }

}
