package com.aaa.api.docs.notification;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.docs.RestDocsIntegrationSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerDocsTest extends RestDocsIntegrationSupport {

    public static final String NOTIFICATION_REDIS_TEST_KEY = "commentNoticeMessage:";

    @Test
    @CustomMockUser
    @DisplayName("sseConnect(): SSE 연결에 성공해야 한다.")
    void test1() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/sse/connect")
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        //then

        assertTrue(content.contains("event:Server Sent Event"));
        assertTrue(content.contains("data:"));

        RestDocumentationResultHandler documentHandler = document("sse-connect-init",
                preprocessResponse(prettyPrint()));
        documentHandler.handle(mvcResult);
    }

}
