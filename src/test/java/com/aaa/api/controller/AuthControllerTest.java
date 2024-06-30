package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.service.dto.response.SessionDataResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("signIn(): 로그인에 성공해 accessToken을 받고 http status code: 200 응답을 받아야 한다.")
    void test1() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .build();

        SessionDataResponse response = SessionDataResponse.builder()
                .id(1L)
                .email("test@test.com")
                .role(Role.ADMIN)
                .name("foo")
                .build();

        when(authService.login(any())).thenReturn(response);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("signIn(): 이메일 형식이 아닌 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test2() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93")
                .password("kdh1234")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .with(csrf())
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
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("signIn(): 이메일을 입력하지않은 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test3() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .password("kdh1234")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .with(csrf())
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
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("signIn(): 비밀번호 형식에 맞지 않는 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test4() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .with(csrf())
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
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("signIn(): 비밀번호 입력이 없는 로그인 요청에는 ErrorMessage를 반환해야한다.")
    void test5() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.password").value("비밀번호를 입력해주세요."))
                .andDo(print());
    }


    @Test
    @CustomMockUser
    @DisplayName("logout(): 로그아웃요청에 쿠키를 만료후 응답해줘야한다.")
    void test7() throws Exception {
        //given
        Cookie cookie = new Cookie("RefreshToken", "refreshTokenCookie");
        cookie.setMaxAge(10000000);
        cookie.setPath("/");

        //when
        ResultActions result = mockMvc.perform(post("/api/logout")
                .with(csrf().asHeader())
                .cookie(cookie)
        );
        //then
        result.andExpect(status().isNoContent())
                .andExpect(cookie().maxAge("RefreshToken", 0))
                .andDo(print());
    }
}