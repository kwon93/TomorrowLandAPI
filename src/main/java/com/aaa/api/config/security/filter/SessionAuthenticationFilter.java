package com.aaa.api.config.security.filter;

import com.aaa.api.exception.MissingRedisSession;
import com.aaa.api.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate redisTemplate;
    @Value("${spring.session.redis.namespace}")
    private  String sessionNamespace;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> sessionValue = extractSessionFromCookie(cookies);

        if (sessionValue.isPresent()) {
            String tomorrowSession = sessionValue.get();
            String session = new String(Base64.getDecoder().decode(tomorrowSession));
            String key = sessionNamespace + ":sessions:" + session;
            String userEmail = (String) redisTemplate.opsForHash().get(key,"sessionAttr:userEmail");
            String userRole = (String) redisTemplate.opsForHash().get(key,"sessionAttr:userRoles");

            if (userEmail.isEmpty()) {
                throw new MissingRedisSession();
            }

            setAuthenticationBySession(userEmail, userRole);
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request,response);
    }

    private Optional<String> extractSessionFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("tomorrowSession"))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void setAuthenticationBySession(String userEmail, String userRole) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        "",
                        List.of(new SimpleGrantedAuthority("ROLE_" + userRole)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
