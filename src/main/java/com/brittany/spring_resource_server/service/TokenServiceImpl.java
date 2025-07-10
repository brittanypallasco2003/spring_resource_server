package com.brittany.spring_resource_server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.brittany.spring_resource_server.controller.Advice.EntityNotFoundException;
import com.brittany.spring_resource_server.controller.Advice.ExpireTokenException;
import com.brittany.spring_resource_server.controller.DTOs.TokenRequest;
import com.brittany.spring_resource_server.controller.DTOs.TokenResponse;
import com.brittany.spring_resource_server.models.RefreshTokenModel;
import com.brittany.spring_resource_server.models.UserModel;
import com.brittany.spring_resource_server.repositories.RefreshTokenRepository;
import com.brittany.spring_resource_server.repositories.UserRepository;

@Service
public class TokenServiceImpl implements TokenService {

  private final JwtEncoder jwtEncoder;

  private final JwtDecoder jwtDecoder;

  private final AuthenticationManager authenticationManager;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepository userRepository;

  public TokenServiceImpl(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager,
      RefreshTokenRepository refreshTokenRepository, JwtDecoder jwtDecoder, UserRepository userRepository) {
    this.jwtEncoder = jwtEncoder;
    this.authenticationManager = authenticationManager;
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtDecoder = jwtDecoder;
    this.userRepository = userRepository;
  }

  @Value("${security.jwt.user.generator}")
  private String userGenerator;

  @Override
  public TokenResponse generateToken(TokenRequest request) {

    String subject = null;
    String scope = null;
    String refreshToken = null;
    Instant now = Instant.now();

    if (request.grantType().equals("password")) {
      // 1. Autenticar usuario
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          request.username(), request.password());

      Authentication userAuthenticated = authenticationManager.authenticate(authenticationToken);

      subject = userAuthenticated.getName();
      scope = userAuthenticated.getAuthorities().stream().map(auth -> auth.getAuthority())
          .collect(Collectors.joining(" "));

      UserModel userDb = userRepository.findByUsername(subject)
          .orElseThrow(() -> new EntityNotFoundException("Usuario no registrado"));

      // Eliminar tokens antiguos
      refreshTokenRepository.deleteByUser(userDb);

      // Crear un nuevo refresh token
      RefreshTokenModel newToken = new RefreshTokenModel();
      newToken.setToken(UUID.randomUUID().toString());
      newToken.setExpiryDate(now.plus(7, ChronoUnit.DAYS));

      userDb.addTokenRefresh(newToken);
      UserModel userUpdated = userRepository.save(userDb);
      refreshToken = newToken.getToken();
    } else if (request.grantType().equals("refreshToken")) {
      RefreshTokenModel storedToken = refreshTokenRepository.findByToken(request.refreshToken())
          .orElseThrow(() -> new EntityNotFoundException("Refresh token inválido"));

      if (storedToken.getExpiryDate().isBefore(now)) {
        refreshTokenRepository.delete(storedToken);
        throw new ExpireTokenException("Refresh token expirado");

      }

      UserModel userDb = storedToken.getUser();
      subject = userDb.getUsername();
      scope = userDb.getRoles().stream()
          .map(role -> role.getRoleName().name())
          .collect(Collectors.joining(" "));

      refreshToken = storedToken.getToken();
    }

    Instant instant = Instant.now();

    JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
        .subject(subject)
        .issuedAt(instant)
        .expiresAt(instant.plus(1, ChronoUnit.HOURS))
        .issuer(userGenerator)
        .claim("authorities", scope)
        .build();

    String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

    return new TokenResponse(accessToken, refreshToken, "Bearer", 3600L);

  }

}
