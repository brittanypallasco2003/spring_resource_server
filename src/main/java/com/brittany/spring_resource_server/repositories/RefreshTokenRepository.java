package com.brittany.spring_resource_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brittany.spring_resource_server.models.RefreshTokenModel;
import com.brittany.spring_resource_server.models.UserModel;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);
    @Modifying
    @Query(value = """
                DELETE FROM refresh_tokens
                WHERE user_id = (SELECT id FROM users WHERE username = :username)
            """, nativeQuery = true)
    void deleteByUsername(@Param("username") String username);
    void deleteByUser(UserModel user);
}
