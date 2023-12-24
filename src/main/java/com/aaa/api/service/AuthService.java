package com.aaa.api.service;

import com.aaa.api.config.data.security.jwt.JwtTokenProvider;
import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.JwtToken;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authenticate
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authenticate);
    }
}
