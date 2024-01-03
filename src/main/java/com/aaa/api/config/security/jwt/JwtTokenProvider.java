package com.aaa.api.config.security.jwt;

import com.aaa.api.domain.enumType.Role;
import com.aaa.api.dto.response.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public JwtToken generateToken(Authentication authentication){
        String  authorities = authentication.getAuthorities().toString();
        log.info("fist auth >>>>> {}",authorities);

        long now = new Date().getTime();

        long expirationDate = 300000L;
        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .expiration(new Date(now + expirationDate))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(authentication.getName())
                .expiration(Date.from(Instant.now().plus(Duration.ofDays(14))))
                .signWith(key)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰");
        }

        String authorities = claims.get("auth").toString();
        log.info("authorities >>>>> {}", authorities);

        //rePlace() 사용안하는 방향으로 리팩토링 해야함.
        authorities = authorities.replaceAll("\\[", "").replaceAll("\\]", "");
        log.info("authorities replace >>>>> {}", authorities);

        User user = new User(claims.getSubject(), "", List.of(new SimpleGrantedAuthority(authorities)));

        return new UsernamePasswordAuthenticationToken(user, "", List.of(new SimpleGrantedAuthority(authorities)));
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }



}
