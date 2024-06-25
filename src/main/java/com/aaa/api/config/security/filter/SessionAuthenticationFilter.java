package com.aaa.api.config.security.filter;

import com.aaa.api.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate<Object , Object > redisTemplate;
    @Value("${spring.session.redis.namespace}")
    private  String sessionNamespace;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionValue = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("tomorrowSession"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        String session = new String(Base64.getDecoder().decode(sessionValue));
        String key = sessionNamespace + ":sessions:" + session;
        String userEmail =  (String) redisTemplate.opsForHash().entries(key).get("sessionAttr:userEmail");
        String userRole =  (String) redisTemplate.opsForHash().entries(key).get("sessionAttr:userRoles");
        if (userEmail.isEmpty()){
            throw new RuntimeException("Redis에 존재하지 않는 세션쓰");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        "",
                        List.of(new SimpleGrantedAuthority("ROLE_" + userRole)));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
