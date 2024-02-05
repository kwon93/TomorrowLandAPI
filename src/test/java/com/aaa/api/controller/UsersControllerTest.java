package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.controller.dto.request.CreateUsersRequest;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import com.aaa.api.service.dto.response.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.aaa.api.domain.Users;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsersControllerTest extends ControllerTestSupport {

    @Test
    @WithMockUser
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

        given(usersService.createUser(any(CreateUsersServiceRequest.class))).willReturn("kwon");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup")
                        .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(usersService, times(1)).createUser(any(CreateUsersServiceRequest.class));
    }


    @Test
    @WithMockUser
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
                        .with(csrf())
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
    @WithMockUser
    @DisplayName("createUser(): 이메일을 작성하지않은 요청에는 ErrorMessage를 반환해야한다.")
    void test3() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .password("kdh12345")
                .name("권동혁")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .with(csrf())
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
    @WithMockUser
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
                        .with(csrf())
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
    @WithMockUser
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
                        .with(csrf())
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


    @Test
    @CustomMockUser
    @DisplayName("rewardPoint(): 답변자 점수 보상에 성공해 http status 201을 응답받는다.")
    void test6() throws Exception {
        //given
        final long postsId = 1L;
        final long testId = 2L;
        final long commentId = 3L;
        doNothing().when(usersService).reward(anyLong(),anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(
                patch("/api/reward/{rewardUserId}/posts/{postsId}/comment/{commentId}",
                testId,postsId, commentId)
                .with(csrf()));

        //then
        result.andExpect(status().isNoContent())
                .andDo(print());
        verify(usersService, times(1)).reward(anyLong(),anyLong(),anyLong());
    }

    @Test
    @WithMockUser
    @DisplayName("createUser(): 이름을 작성하지않은 요청에는 ErrorMessage를 반환해야한다.")
    void test7() throws Exception {
        //given
        CreateUsersRequest request = CreateUsersRequest.builder()
                .email("test@naver.com")
                .password("kdh12345")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.name").value("이름을 입력해주세요."))
                .andDo(print());
    }


    @Test
    @CustomMockUser
    @DisplayName("myPage(): myPage 조회 요청에 성공해 http Status 200을 응답해야한다. ")
    void test8() throws Exception {
        //given
        Users user = Users.builder()
                .email("test@naver.com")
                .password("1234")
                .name("kwon")
                .point(200)
                .userLevel(UserLevel.Beginner)
                .build();


        UserInfo response = UserInfo.builder()
                .entity(user)
                .userAnswer(2)
                .build();

        given(usersService.getUsersInfo(anyLong())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/myPage/{userId}", anyLong())
                .with(csrf().asHeader())
        );
        //then
        result.andExpect(status().isOk())
                .andDo(print());

        verify(usersService, times(1)).getUsersInfo(anyLong());
    }


}