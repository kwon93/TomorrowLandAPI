package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        //then
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
