package com.aaa.api.service;

import com.aaa.api.service.dto.CommentNoticeServiceDto;
import com.aaa.api.service.dto.NoticeMessageData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CommentNotificationService {

    public static final String NOTIFICATION_REDIS_KEY = "commentNoticeMessage:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;

    public String publishCommentNotice(CommentNoticeServiceDto noticeServiceDto) throws JsonProcessingException {
        NoticeMessageData noticeMessageData = processingNoticeMessageData(noticeServiceDto);
        String noticeMessage = objectMapper.writeValueAsString(noticeMessageData);

        String keyByNotice = NOTIFICATION_REDIS_KEY + noticeServiceDto.getPostWriterId();
        redisTemplate.opsForHash().put(keyByNotice, noticeMessageData.getId(), noticeMessage);
        redisTemplate.expire(keyByNotice, 7, TimeUnit.DAYS);

        redisTemplate.convertAndSend(channelTopic.getTopic(), noticeMessage);
        return noticeMessageData.getId();
    }

    private NoticeMessageData processingNoticeMessageData(CommentNoticeServiceDto noticeServiceDto) {
        String noticeMessage = String.format("%s 님이 %s 글에 새 댓글을 작성했습니다.",
                noticeServiceDto.getCommenter(),
                noticeServiceDto.getPostName());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        return NoticeMessageData.builder()
                .id(uuid)
                .noticeMessage(noticeMessage)
                .postWriterId(noticeServiceDto.getPostWriterId())
                .postsId(noticeServiceDto.getPostsId())
                .read(false)
                .build();
    }

}
