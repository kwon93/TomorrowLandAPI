package com.aaa.api.config.security.jwt;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.dto.response.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest extends IntegrationTestSupport {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedKey);
    }

    @Test
    @DisplayName("generateToken(): JWT토큰 생성에 성공해야한다.")
    void test1() throws Exception {
        //given
        Authentication authenticationMock = mock(Authentication.class);

        // when
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticationMock);

        //then
        assertThat(jwtToken).isNotNull();
        assertThat(jwtToken.getGrantType()).isEqualTo("Bearer");
    }


    @Test
    @DisplayName("getAuthentication(): Token에서 Authentication 객체를 얻어야한다.")
    void test2() throws Exception {
        //given
        String accessToken = Jwts.builder()
                .subject("username")
                .claim("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))
                .expiration(new Date(new Date().getTime() + 300000L))
                .signWith(key)
                .compact();
        // when
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //then
        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    @DisplayName("getAuthentication(): claim에 auth가 없는 권한정보가 존재하지않는 토큰일 경우 RuntimeException을 발생시킨다.")
    void test3() throws Exception {
        //given
        String accessToken = Jwts.builder()
                .subject("username")
                .expiration(new Date(new Date().getTime() + 300000L))
                .signWith(key)
                .compact();
        // when
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        });

        //then
        assertThat(e.getMessage()).isEqualTo("권한 정보가 없는 토큰");
    }


    @Test
    @DisplayName("validateToken(): 만료된 토큰일 경우 false를 return해야한다.")
    void test4() throws InterruptedException {
        //given
        Date expirationTime = new Date(System.currentTimeMillis() - SECONDS.toMillis(1));

        String accessToken = Jwts.builder()
                .subject("username")
                .claim("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))
                .expiration(expirationTime)
                .signWith(key)
                .compact();

        // when
        boolean validateResult = jwtTokenProvider.validateToken(accessToken);

        //then
        assertThat(validateResult).isFalse();
    }

    @Test
    @DisplayName("validateToken(): Claim이 비어있는 토큰일 경우 false를 return해야한다.")
    void test5() throws InterruptedException {
        //given
        Date expirationTime = new Date(System.currentTimeMillis() - SECONDS.toMillis(1));

        String accessToken = Jwts.builder()
                .subject("username")
                .expiration(expirationTime)
                .signWith(key)
                .compact();
        // when
        boolean validateResult = jwtTokenProvider.validateToken(accessToken);

        //then
        assertThat(validateResult).isFalse();
    }


}