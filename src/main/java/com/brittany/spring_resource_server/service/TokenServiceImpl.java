package com.brittany.spring_resource_server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Value("${security.jwt.user.generator}")
    private String userGenerator; 

    @Override
    public String generateToken(Authentication authentication) {
        Instant instant= Instant.now();

        String authorities=authentication.getAuthorities().stream().map(auth->auth.getAuthority())
        .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
        .subject(authentication.getName())
        .issuedAt(instant)
        .expiresAt(instant.plus(1, ChronoUnit.HOURS))
        .issuer(userGenerator)
        .claim("authorities", authorities)
        .build();

      return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

    }

}
