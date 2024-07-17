package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.CommentNotice;
import com.aaa.api.controller.dto.request.UpdateCommentNotice;
import com.aaa.api.http.SseEmitters;
import com.aaa.api.service.CommentNotificationService;
import com.aaa.api.service.dto.NoticeMessageDatas;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class NotificationController {

    private final CommentNotificationService commentNotificationService;
    private final SseEmitters sseEmitters;


    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> sseConnect(final CommentNotice commentNotice) throws JsonProcessingException {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitters.add(sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("isSseConnect?")
                    .data("true"));
        } catch (IOException e) {
            //TODO custom으로 수정하기
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/comment/notice")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<NoticeMessageDatas> getStoredNotification(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        NoticeMessageDatas storedNotifications = commentNotificationService.getUnreadNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(storedNotifications);
    }

    @PatchMapping("/comment/notice/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") //permissionEvaluator 추가하기
    public ResponseEntity<Void> updateToNoticeReadStatus(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                         UpdateCommentNotice updateCommentNotice) throws JsonProcessingException {
        commentNotificationService.updateMarkAsRead(userPrincipal.getUserId(), updateCommentNotice.getNoticeId());
        return ResponseEntity.noContent().build();
    }

    //TODO notificationController 분리 및 테스트 진행해야함.
}
