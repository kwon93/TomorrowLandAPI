package com.aaa.api.service;

import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.SseRepository;
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
        return sendToUser(userId, "UserId: "+userId+"'s SSE init");
    }

    private void createEmitter(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);
        sseRepository.save(userId,sseEmitter);

        sseEmitter.onCompletion(() -> sseRepository.deleteById(userId));
        sseEmitter.onTimeout(() -> sseRepository.deleteById(userId));
    }

    public SseEmitter sendToUser(Long userId, String eventData) throws IOException {
        SseEmitter emitter = sseRepository.get(userId);
         if (emitter == null) throw new UserNotFound();

         emitter.send(SseEmitter
                 .event()
                 .name("Server Sent Event")
                 .data(eventData));
         return emitter;
    }
}
