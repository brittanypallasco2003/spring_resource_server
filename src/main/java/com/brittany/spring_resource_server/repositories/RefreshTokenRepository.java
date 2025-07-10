package com.brittany.spring_resource_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brittany.spring_resource_server.models.RefreshTokenModel;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long>{
    Optional<RefreshTokenModel> findByToken(String token);
    void deleteByUsername(String username);

}
