package com.brittany.spring_resource_server.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String getMethodName() {
        return "hola mundo";
    }
    
}
