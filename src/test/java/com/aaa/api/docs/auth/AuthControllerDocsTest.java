package com.aaa.api.docs.auth;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerDocsTest extends RestDocsSupport {
    @Test
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("RestDocs: 로그인 요청 API")
    void test1() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .build();

        given(authService.login(any(LoginServiceRequest.class)))
                .willReturn(JwtToken.builder()
                        .grantType("Bearer")
                        .accessToken("accessToken")
                        .refreshToken("refreshToken")
                .build());

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description("Token GrantType"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT AccessToken"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("JWT RefreshToken")

                        )
                ));
    }



    @Test
    @WithMockUser(username = "kwon93@naver.com", password = "kdh1234", roles = {"ADMIN"})
    @DisplayName("RestDocs: JWT RefreshToken 요청 API")
    void test() throws Exception {
        //given
        given(reIssueProvider.validateRefreshToken(anyString())).willReturn("kwon93@naver.com");
        given(reIssueProvider.reIssueAccessToken(anyString())).willReturn("jwtToken");

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/reissue")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .header("Refresh-Token","refreshToken")
                ).andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(print())
                .andDo(document("auth-refresh",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Refresh-Token").description("JWT RefreshToken")),
                        responseHeaders(
                                headerWithName("Authorization").description("JWT AccessToken"),
                                headerWithName("Refresh-Token").description("JWT RefreshToken"))
                        ));
    }
}
