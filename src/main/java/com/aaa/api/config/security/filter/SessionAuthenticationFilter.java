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

    public static final String SESSION_ATTR_USER_EMAIL = "sessionAttr:userEmail";
    public static final String SESSION_ATTR_USER_ROLES = "sessionAttr:userRoles";
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate redisTemplate;
    @Value("${spring.session.redis.namespace}")
    private  String sessionNamespace;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        Cookie[] sessionCookie = request.getCookies();
        if (sessionCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }
        extractSessionCookie(sessionCookie).ifPresent(this::authenticationProcess);
        filterChain.doFilter(request,response);
    }

    private void authenticationProcess(String session) {
        String key = getSessionRedisKey(session);
        String userEmail = getUserInfoFromRedis(key, SESSION_ATTR_USER_EMAIL);
        String userRole = getUserInfoFromRedis(key, SESSION_ATTR_USER_ROLES);
        setAuthenticationBySession(userEmail, userRole);
    }

    private Optional<String> extractSessionCookie(Cookie[] cookies) {
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

    private String getUserInfoFromRedis(String key, String hashKey) {
        String userInfo = (String) redisTemplate.opsForHash().get(key,hashKey);
        invalidRedisSessionValidation(userInfo);
        return userInfo;
    }

    private String getSessionRedisKey(String session) {
        String decodedSession = new String(Base64.getDecoder().decode(session));
        return sessionNamespace + ":sessions:" + decodedSession;
    }

    private void invalidRedisSessionValidation(String userEmail) {
        if (userEmail == null) {
            throw new MissingRedisSession();
        }
    }

}
