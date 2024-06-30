package com.aaa.api.config;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class CustomMockSecurityContext implements WithSecurityContextFactory<CustomMockUser> {


    @Override
    public SecurityContext createSecurityContext(CustomMockUser annotation) {
        Users user = Users.builder()
                .id(annotation.id())
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .role(annotation.role())
                .build();

        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(annotation.name(),annotation.role().toString(), annotation.id());


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserPrincipal,
                "password",
                List.of(new SimpleGrantedAuthority(customUserPrincipal.getAuthorities().toString())));


        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
