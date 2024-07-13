package com.aaa.api.service;

import com.aaa.api.exception.RedisJsonParsingException;
import com.aaa.api.service.dto.CommentNoticeServiceDto;
import com.aaa.api.service.dto.NoticeMessageData;
import com.aaa.api.service.dto.NoticeMessageDatas;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CommentNotificationService {

    public static final String NOTIFICATION_REDIS_KEY = "commentNoticeMessage:";
    private final RedisTemplate<String, String > redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;

    public void publishCommentNotice(CommentNoticeServiceDto noticeServiceDto) throws JsonProcessingException {
        String noticeMessage = processingNoticeMessageData(noticeServiceDto);

        String keyByNotice = NOTIFICATION_REDIS_KEY + noticeServiceDto.getPostWriterId();
        redisTemplate.opsForList().leftPush(keyByNotice, noticeMessage);
        redisTemplate.expire(keyByNotice, 7, TimeUnit.DAYS);

        redisTemplate.convertAndSend(channelTopic.getTopic(), noticeMessage);
    }

    private String processingNoticeMessageData(CommentNoticeServiceDto noticeServiceDto) throws JsonProcessingException {
        String noticeMessage = String.format("%s 님이 %s 글에 새 댓글을 작성했습니다.", noticeServiceDto.getCommenter(), noticeServiceDto.getPostName());

        NoticeMessageData noticeMessageData = NoticeMessageData.builder()
                .noticeMessage(noticeMessage)
                .postWriterId(noticeServiceDto.getPostWriterId())
                .postsId(noticeServiceDto.getPostsId())
                .build();

        return objectMapper.writeValueAsString(noticeMessageData);
    }


    public NoticeMessageDatas getStoredNotification(Long userId) {
        List<String> jsonNotifications = getNoticeMessageDatasByRedis(userId);

        List<NoticeMessageData> noticeMessageDatas = jsonNotifications.stream()
                .map(this::parseNotification)
                .filter(Objects::nonNull)
                .toList();

        return NoticeMessageDatas.builder()
                                 .noticeMessageDatas(noticeMessageDatas)
                                 .build();
    }

    private List<String> getNoticeMessageDatasByRedis(Long userId) {
        String keyByNotice = NOTIFICATION_REDIS_KEY + userId.toString();
        return redisTemplate.opsForList().range(keyByNotice,0, -1);
    }

    private NoticeMessageData parseNotification(String jsonByRedis) {
        try {
            return objectMapper.readValue(jsonByRedis, NoticeMessageData.class);
        } catch (JsonProcessingException e) {
            throw new RedisJsonParsingException();
        }
    }

}
