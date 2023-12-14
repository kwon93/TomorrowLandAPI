package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.CreateUsersRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsersControllerTest extends ControllerTestSupport {



    @Test
    @DisplayName("signup(): 회원가입 요청에 성공해 http status code: 201 응답을 받아야 한다.")
    void test1() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersRequest request = CreateUsersRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        given(usersService.createUser(any(CreateUsersRequest.class))).willReturn(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(usersService, times(1)).createUser(any(CreateUsersRequest.class));
    }


    @Test
    @DisplayName("createUser(): 이메일 형식이 아닌 요청에는 ErrorMessage를 반환해야한다.")
    void test2() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .email("abc123")
                .password("kdh12345")
                .name("권동혁")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("올바른 E-Mail 형식을 작성해주세요. ex) abc123@gmail.com "))
                .andDo(print());

    }

    @Test
    @DisplayName("createUser(): 이메일을 작성하지않은 요청에는 ErrorMessage를 반환해야한다.")
    void test3() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .password("kdh12345")
                .name("권동혁")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("E-Mail을 작성해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("createUser(): 잘못된 비밀번호 양식 요청에는 ErrorMessage를 반환해야한다.")
    void test4() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .email("kdh93@naver.com")
                .password("kdh")
                .name("권동혁")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.password")
                        .value("비밀번호는 영어 소문자와 숫자의 조합으로 6글자 이상 12글자 미만이어야 합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("createUser(): 이름이 6글자 넘어간 요청에는 ErrorMessage를 반환해야한다.")
    void test5() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .email("kdh93@naver.com")
                .password("kdh1234")
                .name("가짜황금독수리세상을놀라게하다")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.name")
                        .value("당신이 '황금독수리 온 세상을 놀라게하다' 님이 아니시라면 이름은 10글자 미만으로 입력해주세요."))
                .andDo(print());
    }


}