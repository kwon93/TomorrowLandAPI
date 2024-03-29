package com.aaa.api.docs.auth;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.config.RestDocMockUser;
import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(header().string("userId","0"))
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
                        ),
                        responseHeaders(
                                headerWithName("userId").description("사용자의 UniqueID")
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

        Cookie cookie = new Cookie("RefreshToken", "refreshToken");


        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/reissue")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .cookie(cookie)
                ).andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(print())
                .andDo(document("auth-refresh",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestCookies(
                                cookieWithName("RefreshToken").description("JWT RefreshToken")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("JWT AccessToken")
                        ),
                        responseCookies(
                                cookieWithName("RefreshToken").description("JWT RefreshToken")
                        )
                        )
                );
    }


    @Test
    @RestDocMockUser
    @DisplayName("RestDocs: 로그아웃 요청 API")
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
                .andDo(print())
                .andDo(document("auth-logout",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestCookies(
                                cookieWithName("RefreshToken").description("JWT RefreshToken Cookie")
                        ),
                        responseCookies(
                                cookieWithName("RefreshToken").description("Expired JWT RefreshToken Cookie")
                        )
                )
        );
    }
}
