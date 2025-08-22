package com.brittany.spring_resource_server.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hola! Soy público, no necesitas token 👋";
    }

    @GetMapping("/user/hello")
    public String userHello() {
        return "Hola USER 🙋, tienes acceso con ROLE_USER o ROLE_ADMIN";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hola ADMIN 👑, solo accesible con ROLE_ADMIN";
    }

    @GetMapping("/secure/hello")
    public String secureHello() {
        return "Hola! Esta ruta es segura 🔒, necesitas estar autenticado con cualquier rol";
    }

}
