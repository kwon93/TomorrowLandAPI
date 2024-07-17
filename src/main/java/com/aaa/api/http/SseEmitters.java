package com.aaa.api.http;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitters {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);

        //만료 경우
        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
        });

        return emitter;
    }
}
