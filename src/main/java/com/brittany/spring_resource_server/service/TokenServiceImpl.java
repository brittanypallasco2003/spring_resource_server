package com.brittany.spring_resource_server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.brittany.spring_resource_server.controller.Advice.EntityNotFoundException;
import com.brittany.spring_resource_server.controller.Advice.RefreshTokenException;
import com.brittany.spring_resource_server.controller.DTOs.TokenRequest;
import com.brittany.spring_resource_server.controller.DTOs.TokenResponse;
import com.brittany.spring_resource_server.models.GrantTypeEnum;
import com.brittany.spring_resource_server.models.RefreshTokenModel;
import com.brittany.spring_resource_server.models.UserModel;
import com.brittany.spring_resource_server.repositories.RefreshTokenRepository;
import com.brittany.spring_resource_server.repositories.UserRepository;

@Service
public class TokenServiceImpl implements TokenService {

  private final JwtEncoder jwtEncoder;


  private final AuthenticationManager authenticationManager;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepository userRepository;

  public TokenServiceImpl(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager,
      RefreshTokenRepository refreshTokenRepository, JwtDecoder jwtDecoder, UserRepository userRepository) {
    this.jwtEncoder = jwtEncoder;
    this.authenticationManager = authenticationManager;
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  @Value("${security.jwt.user.generator}")
  private String userGenerator;

  @Override
  public TokenResponse generateToken(TokenRequest request) {;
    List<String> dataToken=new ArrayList<>();

    if (request.grantType()==GrantTypeEnum.password) {
      dataToken=generateNewRefreshToken(request);

    } else if (request.grantType()==GrantTypeEnum.refreshToken) {
     dataToken=getStoredRefreshToken(request);
    }

    String subject=dataToken.get(0);
    String authorities=dataToken.get(1);
    String refreshToken=dataToken.get(2);

    Instant instant = Instant.now();

    JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
        .subject(subject)
        .issuedAt(instant)
        .expiresAt(instant.plus(1, ChronoUnit.HOURS))
        .issuer(userGenerator)
        .claim("authorities", authorities)
        .build();

    String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

    return new TokenResponse(accessToken, refreshToken, "Bearer", 3600L);

  }


  private List<String> generateNewRefreshToken(TokenRequest request){
     // 1. Autenticar usuario
      if (request.username() ==null || request.password()==null) {
        throw new IllegalArgumentException("Username y password obligatorios");
      }
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          request.username(), request.password());

      Authentication userAuthenticated = authenticationManager.authenticate(authenticationToken);

      String subject = userAuthenticated.getName();
      String scope = userAuthenticated.getAuthorities().stream().map(auth -> auth.getAuthority())
          .collect(Collectors.joining(" "));

      UserModel userDb = userRepository.findByUsername(subject)
          .orElseThrow(() -> new EntityNotFoundException("Usuario no registrado"));

      // Eliminar tokens antiguos
      refreshTokenRepository.deleteByUser(userDb);

      // Crear un nuevo refresh token
      Instant now = Instant.now();
      RefreshTokenModel newToken = new RefreshTokenModel();
      newToken.setToken(UUID.randomUUID().toString());
      newToken.setExpiryDate(now.plus(7, ChronoUnit.DAYS));

      userDb.addTokenRefresh(newToken);
      UserModel userUpdated = userRepository.save(userDb);
      String refreshToken = newToken.getToken();

      return new ArrayList<>(Arrays.asList(subject, scope, refreshToken));

  }

  private List<String>getStoredRefreshToken(TokenRequest request){
    if (request.refreshToken()==null) {
        throw new IllegalArgumentException("Refresh token es obligatorio para grantType=refreshToken");
      }
      RefreshTokenModel storedToken = refreshTokenRepository.findByToken(request.refreshToken())
          .orElseThrow(() -> new RefreshTokenException("Refresh token inválido"));

      Instant now=Instant.now();
      if (storedToken.getExpiryDate().isBefore(now)) {
        refreshTokenRepository.delete(storedToken);
        throw new RefreshTokenException("Refresh token expirado");

      }

      UserModel userDb = storedToken.getUser();
      String subject = userDb.getUsername();
      String scope = userDb.getRoles().stream()
          .map(role -> role.getRoleName().name())
          .collect(Collectors.joining(" "));

      String refreshToken = storedToken.getToken();
    return new ArrayList<>(Arrays.asList(subject,scope, refreshToken));
  }
}



