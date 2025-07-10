package com.brittany.spring_resource_server.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenModel> refreshTokens;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "user_id", "role_id" }))
    private Set<RoleModel> roles;

    public UserModel() {
        this.roles = new HashSet<>();
        this.refreshTokens = new ArrayList<>();
    }

    @Builder
    public UserModel(String username, String password, Set<RoleModel> roles, List<RefreshTokenModel> refreshTokens) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.refreshTokens = refreshTokens;
    }

    public UserModel addTokenRefresh(RefreshTokenModel refreshTokenModel) {
        refreshTokens.add(refreshTokenModel);
        refreshTokenModel.setUser(this);
        return this;
    }
}
