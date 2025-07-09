package com.brittany.spring_resource_server.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rsa")
public class RsaKeyConfig {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    



}
