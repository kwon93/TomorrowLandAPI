package com.aaa.api.docs.notification;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.docs.RestDocsIntegrationSupport;
import com.aaa.api.service.dto.NoticeMessageData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerDocsTest extends RestDocsIntegrationSupport {

    public static final String NOTIFICATION_REDIS_TEST_KEY = "commentNoticeMessageTEST:";

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


    @Test
    @CustomMockUser
    @DisplayName("getStoredNotification(): 사용자가 읽지않은 댓글들을 불러와야한다.")
    void test2() throws Exception{
        //given
        String keyByNotice = NOTIFICATION_REDIS_TEST_KEY + 1L;
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0, 8);
        IntStream.range(1, 4).mapToObj(index ->
                NoticeMessageData.builder()
                        .noticeMessage("test Message " + index)
                        .read(false)
                        .postWriterId(1L)
                        .id(uuid)
                        .build()
                ).forEach( index -> redisTemplate.opsForHash()
                .put(keyByNotice, String.valueOf(1L), "test Message "+ index ));

        //when
        mockMvc.perform(get("/api/comment/notice"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-unread-notice",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("noticeMessageDatas").description("알림 메시지 데이터의 배열"),
                                fieldWithPath("noticeMessageDatas[].id").description("알림 메시지의 고유 ID"),
                                fieldWithPath("noticeMessageDatas[].noticeMessage").description("알림 메시지 내용"),
                                fieldWithPath("noticeMessageDatas[].postWriterId").description("게시물 작성자의 ID"),
                                fieldWithPath("noticeMessageDatas[].unread").description("알림 메시지의 읽음 여부 (true: 읽지 않음, false: 읽음)")
                        )
                ));
        //then
    }

}
