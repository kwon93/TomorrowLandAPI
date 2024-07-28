package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.service.dto.CommentNoticeServiceDto;
import com.aaa.api.service.dto.NoticeMessageData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CommentNotificationServiceTest extends IntegrationTestSupport {


    private static final String NOTIFICATION_REDIS_KEY = "commentNoticeMessage:";

    @Test
    @DisplayName("publishCommentNotice(): 알림메시지가 Reids에 저장되어야한다.")
    void test1() throws IOException {
        //given
        final String testPostName = "foo title";
        final String testCommenter = "the Commenter bar";

        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);

        SseEmitter sseEmitter = new SseEmitter();
        sseRepository.save(userInTest.getId(), sseEmitter);

        CommentNoticeServiceDto request = CommentNoticeServiceDto.builder()
                .postName(testPostName)
                .commenter(testCommenter)
                .postsId(postInTest.getId())
                .postWriterId(userInTest.getId())
                .build();
        //when
        String noticeUUID = commentNotificationService.publishCommentNotice(request);

        //then
        NoticeMessageData  storedNotice =  (NoticeMessageData) redisTemplate.opsForHash().get(NOTIFICATION_REDIS_KEY + userInTest.getId(), noticeUUID);

        assertThat(storedNotice).isNotNull();
        assertThat(storedNotice.getPostWriterId()).isEqualTo(userInTest.getId());
    }


}