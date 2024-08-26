package com.aaa.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class SseRepository {

    private final Map<Long, SseEmitter>  sseEmitter = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter sseEmitter) {
        this.sseEmitter.put(id, sseEmitter);
    }

    public void deleteById(Long id) {
        this.sseEmitter.remove(id);
    }

    public SseEmitter get(Long id) {
        return this.sseEmitter.get(id);
    }
}
