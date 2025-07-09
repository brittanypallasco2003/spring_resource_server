package com.brittany.spring_resource_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.brittany.spring_resource_server.config.RsaKeyConfig;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyConfig.class)
public class SpringResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringResourceServerApplication.class, args);
	}

}
