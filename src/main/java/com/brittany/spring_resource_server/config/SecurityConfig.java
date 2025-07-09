package com.brittany.spring_resource_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .csrf(csrf->csrf.disable())
        .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
        .oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()))
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .build();

    }

    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password("{noop}1234").authorities("USER").build(),
                User.withUsername("user2").password("{noop}1234").authorities("USER").build(),
                User.withUsername("user3").password("{noop}1234").authorities("USER","ADMIN").build());
    }
    
    // Decodifica y valida tokens JWT entrantes (Usan la clave pública)
    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyConfig.getPublicKey()).build();
    }

    // Genera y firma los tokens jwt salientes (Clave privada y pública)
    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk= new RSAKey.Builder(rsaKeyConfig.getPublicKey()).privateKey(rsaKeyConfig.getPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);
    }


}
