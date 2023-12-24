package com.aaa.api.config.data.security.jwt;

import com.aaa.api.dto.response.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private SecretKey key;
    private @Value("${jwt.expiration}") Long expirationDate;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public JwtToken generateToken(Authentication authentication){
        String  authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();

        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .expiration(new Date(now + expirationDate))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .expiration(new Date(now + expirationDate))
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
            throw new RuntimeException("권한정보없는 토큰");
        }

        List<SimpleGrantedAuthority> authorities
                = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        User user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, "",authorities);
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
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
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
