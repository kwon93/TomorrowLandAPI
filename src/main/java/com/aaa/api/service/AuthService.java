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
    private final RedisTemplate<String, Object> redisTemplate;

    public long getUserId(final LoginServiceRequest serviceRequest){
        Users users = usersRepository.findByEmail(serviceRequest.getEmail())
                .orElseThrow(UserNotFound::new);
        return users.getId();
    }

    @Transactional
    public SessionDataResponse login(LoginServiceRequest request) {
        Users userByEmail =
                usersRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidSignInInfomation::new);

        if(!passwordEncoder.matches(request.getPassword(), userByEmail.getPassword())) {
            throw new InvalidSignInInfomation();
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userByEmail.getEmail());

        //TODO @Cacheable 로 refactoring 해보자
        redisTemplate.opsForHash().put("userInfo","userId", userByEmail.getId());
        redisTemplate.opsForHash().put("userInfo","username", userByEmail.getEmail());
        redisTemplate.opsForHash().put("userInfo","userRole", userByEmail.getRoles());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        "",
                        List.of(new SimpleGrantedAuthority("ROLE_"+userByEmail.getRoles().toString())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return SessionDataResponse.of(userByEmail);
    }
}
