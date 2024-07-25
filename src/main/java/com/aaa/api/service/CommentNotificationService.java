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

import java.util.*;
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

    private NoticeMessageData processingNoticeMessageData(CommentNoticeServiceDto noticeServiceDto) throws JsonProcessingException {
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


    public NoticeMessageDatas getUnreadNotifications(Long userId) {
        Map<Object, Object> noticeMessageInMap = getNoticeMessageDatasByRedis(userId);

        List<NoticeMessageData> noticeMessageDataCollection = noticeMessageInMap.values().stream().map(this::parseNotification).filter(Objects::nonNull).filter(NoticeMessageData::isUnread).toList();

        return NoticeMessageDatas.builder().noticeMessageDatas(noticeMessageDataCollection).build();
    }

    private Map<Object, Object> getNoticeMessageDatasByRedis(Long userId) {
        String keyByNotice = NOTIFICATION_REDIS_KEY + userId.toString();
        return redisTemplate.opsForHash().entries(keyByNotice);
    }

    public void updateMarkAsRead(Long userId, String noticeId) throws JsonProcessingException {
        String keyByNotice = NOTIFICATION_REDIS_KEY + userId.toString();
        String unreadNotice = (String) redisTemplate.opsForHash().get(keyByNotice, noticeId);
        NoticeMessageData noticeMessageData = parseNotification(unreadNotice);

        noticeMessageData.markAsRead();
        updateNoticeMessage(noticeId, noticeMessageData, keyByNotice);
    }

    //밀린 알림 기능들 삭제 고려..
    private NoticeMessageData parseNotification(Object jsonByRedis) {
        try {
            return objectMapper.readValue((String) jsonByRedis, NoticeMessageData.class);
        } catch (JsonProcessingException e) {
            throw new RedisJsonParsingException();
        }
    }

    private void updateNoticeMessage(String noticeId, NoticeMessageData noticeMessageData, String keyByNotice) throws JsonProcessingException {
        String readNotice = objectMapper.writeValueAsString(noticeMessageData);
        redisTemplate.opsForHash().put(keyByNotice, noticeId, readNotice);
    }
}
