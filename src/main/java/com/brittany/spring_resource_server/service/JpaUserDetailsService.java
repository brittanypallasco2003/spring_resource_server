package com.brittany.spring_resource_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brittany.spring_resource_server.models.UserModel;
import com.brittany.spring_resource_server.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userDb= userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(String.format("El usuario %s no existe en el sistema!",username)));

        List<GrantedAuthority> authorities=userDb
        .getRoles()
        .stream()
        .map(roleModel->new SimpleGrantedAuthority("ROLE_".concat(roleModel.getRoleName().name())))
        .collect(Collectors.toList());
      
        return new User(userDb.getUsername(), userDb.getPassword(), authorities);
        
    }
    
}
