package com.aaa.api.config.security;

import com.aaa.api.config.security.permission.TomorrowLandPermissionEvaluator;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final PostsRepository postsRepository;
    private final CommentRepository commentRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(){
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new TomorrowLandPermissionEvaluator(postsRepository,commentRepository));
        return handler;
    }

}
