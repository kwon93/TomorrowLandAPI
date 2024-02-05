package com.aaa.api.service;

import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.config.security.jwt.JwtTokenReIssueProvider;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.LoginServiceRequest;
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

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenReIssueProvider reIssueProvider;

    public JwtToken login(final LoginServiceRequest serviceRequest) {
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(serviceRequest.getEmail(), serviceRequest.getPassword());

            final Authentication authenticate
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authenticate);
    }

    public long getUserId(final LoginServiceRequest serviceRequest){
        Users users = usersRepository.findByEmail(serviceRequest.getEmail())
                .orElseThrow(UserNotFound::new);
        return users.getId();
    }

    public String reissueAccessToken(final String refreshToken) {
        final String username = reIssueProvider.validateRefreshToken(refreshToken);
        return reIssueProvider.reIssueAccessToken(username);
    }
}
