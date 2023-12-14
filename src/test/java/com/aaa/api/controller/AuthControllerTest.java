package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.dto.request.CreateUsersRequest;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableConfigurationProperties
class AuthControllerTest extends ControllerTestSupport {

    @Value("${aaa.jwt-key}")
    private String secretKey;

    @Test
    @DisplayName("signIn(): 로그인에 성공해 http status code: 200 응답을 받아야 한다.")
    void test1() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .build();

        given(authService.login(any(LoginRequest.class))).willReturn(1L);
        given(ymlProperties.getJwtKey()).willReturn(secretKey);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(authService, times(1))
                .login(argThat(arg ->
                        arg.getEmail().equals(request.getEmail()) &&
                                arg.getPassword().equals(request.getPassword())));
    }

    @Test
    @DisplayName("signIn(): 이메일 형식이 아닌 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test2() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93")
                .password("kdh1234")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("올바른 E-Mail 형식을 입력해주세요. ex) abc123@gmail.com "))
                .andDo(print());
    }

    @Test
    @DisplayName("signIn(): 이메일을 입력하지않은 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test3() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .password("kdh1234")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("E-Mail을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("signIn(): 비밀번호 형식에 맞지 않는 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test4() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 영어 소문자와 숫자의 조합으로 6글자 이상 12글자 미만이어야 합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("signIn(): 비밀번호 입력이 없는 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test5() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.password").value("비밀번호를 입력해주세요."))
                .andDo(print());
    }






}