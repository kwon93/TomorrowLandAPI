package com.aaa.api.config.security.jwt;

import com.aaa.api.service.dto.response.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final long EXPIRATION_DATE = 300000L;
    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secretKey}") final String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public JwtToken generateToken(final Authentication authentication){
        final String  authorities = authentication.getAuthorities().toString();
        log.info("fist auth >>>>> {}",authorities);

        final long now = new Date().getTime();

        final String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .expiration(new Date(now + EXPIRATION_DATE))
                .signWith(key)
                .compact();

        final String refreshToken = Jwts.builder()
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

    public Authentication getAuthentication(final String accessToken){
        final Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null){
            //CustomException TODO
            throw new RuntimeException("권한 정보가 없는 토큰");
        }

        String authorities = claims.get("auth").toString();
        log.info("authorities >>>>> {}", authorities);
        //rePlace() 사용안하는 방향으로 리팩토링 해야함. TODO
        authorities = authorities.replaceAll("\\[", "").replaceAll("\\]", "");
        log.info("authorities replace >>>>> {}", authorities);

        final User user = new User(claims.getSubject(), "", List.of(new SimpleGrantedAuthority(authorities)));

        return new UsernamePasswordAuthenticationToken(user, "", List.of(new SimpleGrantedAuthority(authorities)));
    }



    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
            //CustomException TODO
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
