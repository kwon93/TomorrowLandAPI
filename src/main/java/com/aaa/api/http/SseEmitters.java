package com.aaa.api.http;

import com.aaa.api.exception.UserNotFound;
import com.aaa.api.service.dto.NoticeMessageData;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {

    private final Map<Long ,SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long userId, SseEmitter emitter) {
        this.emitters.put(userId,emitter);

        //만료 경우
        emitter.onCompletion(() -> this.emitters.remove(userId));
        emitter.onTimeout(emitter::complete);

        return emitter;
    }

    public void sendToUser(NoticeMessageData noticeMessageData) throws IOException {
        SseEmitter userEmitter = this.emitters.get(noticeMessageData.getPostWriterId());
        if (emitters.isEmpty()) throw new UserNotFound();

        userEmitter.send(SseEmitter.event().name("CommentNotice").data(noticeMessageData.getNoticeMessage()));
    }
}
