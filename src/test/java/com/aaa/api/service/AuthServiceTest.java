package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.JwtToken;
import com.aaa.api.exception.InvalidSignInInfomation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest extends IntegrationTestSupport {



    @Test
    @DisplayName("login(): 요청에 맞는 로그인에 성공해 사용자의 JWT Token을 반환한다.")
    void test1() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginRequest request = LoginRequest.builder()
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

        LoginRequest request = LoginRequest.builder()
                .email("invalid@naver.com")
                .password("kdh1234")
                .build();

        // when
        InvalidSignInInfomation e = assertThrows(InvalidSignInInfomation.class, () -> {
            authService.login(request);
        });

        assertThat(e.getMessage()).isEqualTo("이메일 및 비밀번호가 일치하지않습니다.");
    }

    @Test
    @DisplayName("login(): 비밀번호가 틀릴경우 로그인에 실패해 InvalidSigninInfomataionException을 반환한다.")
    void test3() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("invalidPassword")
                .build();

        // when
        InvalidSignInInfomation e = assertThrows(InvalidSignInInfomation.class, () -> {
            authService.login(request);
        });

        assertThat(e.getMessage()).isEqualTo("이메일 및 비밀번호가 일치하지않습니다.");
    }
}