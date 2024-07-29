package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.SessionDataResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest extends IntegrationTestSupport {


    @Test
    @DisplayName("login(): 요청에 맞는 로그인에 성공해 SessionDataResponse를 반환한다.")
    void test1() throws Exception {
        //given
        Users userInTest = createUserInTest();

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .build();
        // when
        SessionDataResponse login = authService.processingUserSessionBy(request);

        //then
        assertThat(login.getEmail()).isEqualTo(request.getEmail());
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
        UserNotFound e = assertThrows(UserNotFound.class, () -> {
            authService.processingUserSessionBy(request);
        });

        assertThat(e.getMessage()).isEqualTo("DB에서 찾을 수 없는 사용자 정보");
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
            authService.processingUserSessionBy(request);
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
