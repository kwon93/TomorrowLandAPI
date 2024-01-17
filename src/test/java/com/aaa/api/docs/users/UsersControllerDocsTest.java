package com.aaa.api.docs.users;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.controller.dto.request.CreateUsersRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersControllerDocsTest extends RestDocsSupport {

    @Test
    @WithMockUser
    @DisplayName("RestDocs:회원 가입 요청 API")
    void test1() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersRequest request = CreateUsersRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.ADMIN)
                .build();

        given(usersService.createUser(any(CreateUsersServiceRequest.class))).willReturn("ADMIN");

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/signup")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                        .andDo(MockMvcRestDocumentation.document("users-signup",
                                preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("role").type(JsonFieldType.STRING).description("사용자 권한")
                                )
                                ));
    }


    @Test
    @CustomMockUser
    @DisplayName("RestDocs: 답변자 보상 API")
    void test6() throws Exception {
        //given
        final long postsId = 1L;
        final long testId = 2L;
        final long commentId = 3L;
        doNothing().when(usersService).reward(anyLong(),anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/reward/{rewardUserId}/posts/{postsId}/comment/{commentId}",
                        testId,postsId, commentId)
                        .with(csrf().asHeader()));

        //then
        result.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("users-reward",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("rewardUserId").description("점수를 받는 사용자 아이디"),
                                parameterWithName("postsId").description("해당 게시물 번호"),
                                parameterWithName("commentId").description("해당 댓글 번호")
                        )
                        ));
    }
}
