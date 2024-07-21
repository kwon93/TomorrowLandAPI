package com.aaa.api.http;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.domain.Users;
import com.aaa.api.repository.SseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockAsyncContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class SseServiceTest extends IntegrationTestSupport {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SseRepository sseRepository;



    @Test
    @CustomMockUser
    @DisplayName("")
    void test1() throws Exception {
        //given
        // 1. SSE 연결
        MvcResult sseResult = mockMvc.perform(get("/api/sse/connect")
                        .contentType(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        // 2. 댓글 작성 API 호출
        Users userInTest = createUserInTest();
        createPostInTest(userInTest);
        CreateCommentRequest request = CreateCommentRequest.builder()
                .content("댓글")
                .build();

        mockMvc.perform(post("/api/posts/{postsId}/comment", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        //when
        CountDownLatch latch = new CountDownLatch(1);
        StringBuffer assertData = new StringBuffer();

        new Thread(() -> {
            try {
                MockAsyncContext asyncContext = (MockAsyncContext) sseResult.getRequest().getAsyncContext();
                while (asyncContext.getDispatchedPath() == null) {
                    Thread.sleep(1000);
                }
                String response = sseResult.getResponse().getContentAsString();
                log.info("async response >>>>>>> {}", response);

                if(response.contains("data:")) {
                    assertData.append(response);
                    latch.countDown();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        //then
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        String receivedEvent = assertData.toString();
        assertTrue(receivedEvent.contains("data:"));
    }
}
