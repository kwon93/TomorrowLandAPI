package com.aaa.api.service;

import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.SseRepository;
import com.aaa.api.service.dto.NoticeMessageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseService {

    private static final Long SSE_TIMEOUT = 60L * 1000 * 60;
    private final SseRepository sseRepository;

    public SseEmitter connectToSSE(final Long userId) throws IOException {
        createEmitter(userId);
        return sendSseFirstInitMessage(userId, "UserId: "+userId+"'s SSE init");
    }

    public void sendToUser(NoticeMessageData noticeMessageData) throws IOException {
        SseEmitter emitter = findEmitterById(noticeMessageData.getPostWriterId());
        emitter.send(SseEmitter
                 .event()
                 .name("Server Sent Event")
                 .data(noticeMessageData.getNoticeMessage()));
    }

    private void createEmitter(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);
        sseRepository.save(userId,sseEmitter);
        sseEmitter.onCompletion(() -> sseRepository.deleteById(userId));
        sseEmitter.onTimeout(() -> sseRepository.deleteById(userId));
    }

    private SseEmitter sendSseFirstInitMessage(final Long userId, String initMessage) throws IOException {
        SseEmitter emitter = findEmitterById(userId);
        emitter.send(SseEmitter
                .event()
                .name("Server Sent Event")
                .data(initMessage));
        return emitter;
    }


    private SseEmitter findEmitterById(final Long userId) {
        SseEmitter emitter = sseRepository.get(userId);
        if (emitter == null) throw new UserNotFound();
        return emitter;
    }
}
