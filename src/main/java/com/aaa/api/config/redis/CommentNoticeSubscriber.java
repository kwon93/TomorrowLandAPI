package com.aaa.api.config.redis;

import com.aaa.api.exception.FailToSendNotificationException;
import com.aaa.api.service.SseService;
import com.aaa.api.service.dto.NoticeMessageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentNoticeSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseService sseService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channelMessage = getChannelMessage(message);
        sendMessageToSSE(channelMessage);
    }

    private void sendMessageToSSE(String channelMessage) {
        try {
            NoticeMessageData noticeMessageData = objectMapper.readValue(channelMessage, NoticeMessageData.class);
            sseService.sendToUser(noticeMessageData);
        } catch (IOException ioException) {
            throw new FailToSendNotificationException(ioException);
        }
    }

    private String getChannelMessage(Message message) {
        return new String(message.getBody());
    }
}
