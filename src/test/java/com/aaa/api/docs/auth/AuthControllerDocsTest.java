package com.aaa.api.docs.auth;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.docs.RestDocsIntegrationSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerDocsTest extends RestDocsIntegrationSupport {

    @Autowired
    SessionRepository sessionRepository;

    @Test
    @DisplayName("RestDocs: 로그인 요청 API")
    void test1() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("async@naver.com")
                .password("kdh1234")
                .build();

        // when then
        mockMvc.perform(post("/api/login")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("tomorrowSession"))
                .andExpect(cookie().secure("tomorrowSession", true))
                .andExpect(cookie().httpOnly("tomorrowSession", true))
                .andDo(print())
                .andDo(document("auth-login",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                        ),
                        responseCookies(
                                cookieWithName("tomorrowSession").description("인증 정보가 담긴 세션 Attribute: userEmail, userRole ")
                        )
                ));
    }


    @Test
    @DisplayName("RestDocs: 로그아웃 요청 API")
    void test7() throws Exception {
        //given
        Session session = sessionRepository.createSession();
        session.setAttribute("userEmail", "foo@bar.com");
        sessionRepository.save(session);

        String encodedSessionId = Base64.getEncoder().encodeToString(session.getId().getBytes());
        Cookie tomorrowSession = new Cookie("tomorrowSession", encodedSessionId);

//        when
        ResultActions result = mockMvc.perform(post("/api/logout")
                        .with(request -> request)
                        .cookie(tomorrowSession));
//        then
        result.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("auth-logout",
                        preprocessResponse(prettyPrint()),
                        requestCookies(
                                cookieWithName("tomorrowSession").description("인증에 사용됬던 사용자의 권한")
                        )
                )
        );
    }
}
