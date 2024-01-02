package com.aaa.api.config.security;


import com.aaa.api.domain.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal extends User {

    @Getter
    private final Long userId;
    public CustomUserPrincipal(Users users) {
        super(users.getEmail(), users.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+users.getRoles().value())));

        userId = users.getId();
    }

    public static CustomUserPrincipal of(Users users){
        return new CustomUserPrincipal(users);
    }
}
