package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.exception.InvalidSignInInfomation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest extends IntegrationTestSupport {

    @BeforeEach
    void setUp() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedKey);
    }

    @Test
    @DisplayName("login(): 요청에 맞는 로그인에 성공해 사용자의 JWT Token을 반환한다.")
    void test1() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .build();


        // when
        JwtToken jwtToken = authService.login(request);
        //then
        assertThat(jwtToken.getGrantType()).isEqualTo("Bearer");
    }


    @Test
    @DisplayName("login(): 아이디가 틀릴경우 로그인에 실패해 InvalidSigninInfomataionException을 반환한다.")
    void test2() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("invalid@naver.com")
                .password("kdh1234")
                .build();

        // when
        InvalidSignInInfomation e = assertThrows(InvalidSignInInfomation.class, () -> {
            authService.login(request);
        });

        assertThat(e.getMessage()).isEqualTo("이메일 또는 비밀번호 인증 실패");
    }

    @Test
    @DisplayName("login(): 비밀번호가 틀릴경우 로그인에 실패해 InvalidSigninInfomataionException을 반환한다.")
    void test3() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("kwon93@naver.com")
                .password("invalidPassword")
                .build();

        // when
        InvalidSignInInfomation e = assertThrows(InvalidSignInInfomation.class, () -> {
            authService.login(request);
        });

        assertThat(e.getMessage()).isEqualTo("이메일 또는 비밀번호 인증 실패");
    }


    @Test
    @DisplayName("reissueAccessToken(): RefreshToken을 활용해 AccessToken 재발급에 성공한다.")
    void test4() {
        //given

        Users userInTest = createUserInTest();

        String refreshToken = Jwts.builder()
                .subject(userInTest.getEmail())
                .expiration(Date.from(Instant.now().plus(Duration.ofDays(14))))
                .signWith(key)
                .compact();
        // when
        String accessToken = authService.reissueAccessToken(refreshToken);
        //then
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(userInTest.getEmail());
    }


    @Test
    @DisplayName("getUserId(): 사용자의 UniqueId를 가져오는데 성공한다.")
    void test5() {
        //given
        Users userInTest = createUserInTest();
        LoginServiceRequest request = LoginServiceRequest.builder()
                .email(userInTest.getEmail())
                .password(userInTest.getPassword())
                .build();

        // when
        long userId = authService.getUserId(request);
        //then
        assertThat(userId).isEqualTo(userInTest.getId());

    }
}
