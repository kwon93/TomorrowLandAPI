package com.aaa.api.config.security.jwt;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenReIssueProviderTest extends IntegrationTestSupport {

    @Autowired
    JwtTokenReIssueProvider reIssueProvider;
    @Value("${jwt.secretKey}")
    private String secretKey;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedKey);
    }

   @Test
   @DisplayName("reIssueAccessToken(): RefreshToken을 통해 AccessToken 재발급에 성공해야한다.")
   void test() {
       //given
       Users userInTest = createUserInTest();
       // when
       String accessToken = reIssueProvider.reIssueAccessToken(userInTest.getEmail());

       //then
       Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
       assertThat(authentication.isAuthenticated()).isTrue();
   }


   @Test
   @DisplayName("reIssueAccessToken(): DB에 존재하지않는 회원일 경우 AccessToken 발급에 실패해야한다.")
   void test3() {
       // expected
       assertThatThrownBy(()-> reIssueProvider.reIssueAccessToken("invalid@test.com"))
               .isInstanceOf(UsernameNotFoundException.class)
               .hasMessage("찾을 수 없는 회원입니다.");
   }


   @Test
   @DisplayName("validateRefreshToken(): RefreshToken 검증에 성공해 사용자 이메일을 반환한다.")
   void test2() {
       //given
       Users userInTest = createUserInTest();

       String refreshToken = Jwts.builder()
               .subject(userInTest.getEmail())
               .expiration(Date.from(Instant.now().plus(Duration.ofDays(14))))
               .signWith(key)
               .compact();

       // when
       String username = reIssueProvider.validateRefreshToken(refreshToken);

       //then
       assertThat(username).isEqualTo(userInTest.getEmail());
   }


    @Test
    @DisplayName("validateToken(): 만료된 RefreshToken일 경우 ExpiredJwtException이 발생 해야한다.")
    void test4() throws InterruptedException {
        //given
        Date expirationTime = new Date(System.currentTimeMillis() - SECONDS.toMillis(1));

        String refreshToken = Jwts.builder()
                .subject("username")
                .expiration(expirationTime)
                .signWith(key)
                .compact();

        // when
        assertThatThrownBy(()-> reIssueProvider.validateRefreshToken(refreshToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("validateToken(): 위조된 SecretKey로 서명된 RefreshToken일 경우 JwtException이 발생 해야한다.")
    void test5() throws InterruptedException {
        //given
        final String hackersKey = "b212c2Rha2x2anNkYWw7ZmpzZGtsZmpzZGFsa2ZqZHNsO2FrZmpkc2xrO2Zqc2RhbGs7Zmpkc2E7Cg";
        byte[] decode = Base64.getDecoder().decode(hackersKey);
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor(decode);

        String refreshToken = Jwts.builder()
                .subject("username")
                .expiration(new Date(new Date().getTime() + 300000L))
                .signWith(invalidSecretKey)
                .compact();

        // when
        assertThatThrownBy(()-> reIssueProvider.validateRefreshToken(refreshToken))
                .isInstanceOf(JwtException.class);
    }

}