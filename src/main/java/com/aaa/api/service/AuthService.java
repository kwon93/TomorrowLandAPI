package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.SessionDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate redisTemplate;

    @Transactional
    public SessionDataResponse signInProcess(final LoginServiceRequest request) {
        Users userByEmail = findUserByEmail(request);
        invalidSignInValidation(request, userByEmail);
        userInfoStoreToRedis(userByEmail);
        setAuthenticateBy(userByEmail);

        return SessionDataResponse.of(userByEmail);
    }

    public long getUserId(final LoginServiceRequest serviceRequest) {
        Users users = findUserByEmail(serviceRequest);
        return users.getId();
    }

    private void setAuthenticateBy(Users userByEmail) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userByEmail.getEmail());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        "",
                        List.of(new SimpleGrantedAuthority("ROLE_" + userByEmail.getRoles().toString())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void userInfoStoreToRedis(Users userByEmail) {
        redisTemplate.opsForHash().put("userInfo", "userId", userByEmail.getId());
        redisTemplate.opsForHash().put("userInfo", "username", userByEmail.getEmail());
        redisTemplate.opsForHash().put("userInfo", "userRole", userByEmail.getRoles());
    }

    private Users findUserByEmail(LoginServiceRequest request) {
        return usersRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFound::new);
    }

    private void invalidSignInValidation(LoginServiceRequest request, Users userByEmail) {
        if (isNotPasswordMatch(request, userByEmail)) {
            throw new InvalidSignInInfomation();
        }
    }

    private boolean isNotPasswordMatch(LoginServiceRequest request, Users userByEmail) {
        return !passwordEncoder.matches(request.getPassword(), userByEmail.getPassword());
    }
}
