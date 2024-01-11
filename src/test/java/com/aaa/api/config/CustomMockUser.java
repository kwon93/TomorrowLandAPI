package com.aaa.api.config;

import com.aaa.api.domain.enumType.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockSecurityContext.class)
public @interface CustomMockUser {

    String email() default "kwon93@naver.com";
    String name() default "권동혁";
    String password() default "kdh1234";
    Role role() default Role.ADMIN;
    long id() default 1L;
}
