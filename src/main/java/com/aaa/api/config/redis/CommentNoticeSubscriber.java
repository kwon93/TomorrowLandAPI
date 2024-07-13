package com.aaa.api.config.redis;

import com.aaa.api.exception.RedisJsonParsingException;
import com.aaa.api.service.dto.NoticeMessageData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentNoticeSubscriber implements MessageListener {
    
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channelMessage = new String(message.getBody());
        try {
            NoticeMessageData noticeMessageData = objectMapper.readValue(channelMessage, NoticeMessageData.class);

            messageSendingOperations.convertAndSendToUser(
                        noticeMessageData.getPostWriterId().toString(),
                        "/queue/notifications",
                        noticeMessageData
                        );
        } catch (JsonProcessingException e) {
            throw new RedisJsonParsingException();
        }
    }
}
