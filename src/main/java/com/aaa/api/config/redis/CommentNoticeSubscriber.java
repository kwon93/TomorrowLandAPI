package com.aaa.api.config.redis;

import com.aaa.api.exception.RedisJsonParsingException;
import com.aaa.api.http.SseEmitters;
import com.aaa.api.service.dto.NoticeMessageData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CommentNoticeSubscriber implements MessageListener {
    
    private final ObjectMapper objectMapper;
    private final SseEmitters sseEmitters;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channelMessage = new String(message.getBody());
        try {
            NoticeMessageData noticeMessageData = objectMapper.readValue(channelMessage, NoticeMessageData.class);
            sseEmitters.sendToUser(noticeMessageData);
        } catch (JsonProcessingException e) {
            throw new RedisJsonParsingException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
