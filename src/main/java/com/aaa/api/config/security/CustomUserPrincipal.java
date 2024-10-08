package com.aaa.api.config.security;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomUserPrincipal extends User {

    @Getter
    private final Long userId;

    public CustomUserPrincipal(final String username, final String userRole, Long userId) {
        super(username, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole)));
        this.userId = userId;
    }
}
