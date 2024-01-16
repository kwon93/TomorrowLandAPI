package com.aaa.api.config;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.domain.Users;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class RestDocMockSecurityContext implements WithSecurityContextFactory<RestDocMockUser> {

    @Override
    public SecurityContext createSecurityContext(RestDocMockUser annotation) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(annotation.email(),
                "password",
                List.of(new SimpleGrantedAuthority(annotation.role().toString())));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        return context;
    }

}
