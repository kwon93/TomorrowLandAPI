package com.aaa.api.config.security;


import com.aaa.api.domain.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal extends User {
    public CustomUserPrincipal(Users users) {
        super(users.getEmail(), users.getPassword(), List.of(new SimpleGrantedAuthority(users.getRoles().value())));
    }

    public CustomUserPrincipal of(Users users){
        return new CustomUserPrincipal(users);
    }
}
